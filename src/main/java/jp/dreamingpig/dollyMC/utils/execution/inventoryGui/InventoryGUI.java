package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import jp.dreamingpig.dollyMC.utils.execution.AbstractExecution;
import jp.dreamingpig.dollyMC.utils.execution.ExecutionScenario;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * クリック操作ができるインベントリGUIの定義です。
 * この定義をもとに{@link InventoryGUIInstance}が生成されます。
 */
public class InventoryGUI<Container> {
    /**
     * プレイヤーインベントリを操作しようとしたときに呼び出されます。操作しようとしているスロットの位置も受け取ります。
     * プレイヤーインベントリの編集を許可する場合、trueを返してください。
     * @param <Container>
     */
    public interface PlayerAreaEditor<Container>{
        boolean onEdit(InventoryGUIInstance<Container> instance, int slot);
    }
    public interface GUIClickEvent<Container>{
        void onClick(InventoryGUIInstance<Container> instance);
    }
    public interface GUIClickInAreaEvent<Container>{
        void onClick(InventoryGUIInstance<Container> instance, int index);
    }

    /**
     * インタラクションが発生したときのイベントを表します。
     * カーソルのアイテムを書き換えない場合はnullを返します。
     * @param <Container>
     */
    public interface GUIClickInInteractiveAreaEvent<Container>{
        @Nullable ItemStack onClick(InventoryGUIInstance<Container> instance, @NotNull ItemStack cursor, int index);
    }
    public record SlotInfo(int slot){
        public int line(){
            return slot() / 9;
        }
        public int slotOnLine(){
            return slot() % 9;
        }
    }
    public interface SlotPredicate{
        boolean predicate(SlotInfo slot);
    }

    public record GUIFixedItem(SlotPredicate predicate, ItemStack item){}
    public record GUIStaticItem<Container>(int slot, ItemStack item, GUIClickEvent<Container> onClick){}
    public record GUIDynamicItem<Container>(int slot, Function<InventoryGUIInstance<Container>, ItemStack> item, GUIClickEvent<Container> onClick){}
    public record GUIAreaItem<Container>(SlotPredicate predicate, BiFunction<InventoryGUIInstance<Container>, Integer, @Nullable ItemStack> item, GUIClickInAreaEvent<Container> onClick){
        /**
         * スロットの位置をエリア上の位置に変換します。
         * @param slot インベントリGUI上での位置
         * @return エリア上の位置
         */
        public int getIndex(int slot){
            int index = 0;
            for(int i = 0;i<slot;i++){
                if(predicate.predicate(new SlotInfo(i))) index++;
            }
            return index;
        }
    }
    public record GUIInteractiveAreaItem<Container>(SlotPredicate predicate, BiFunction<InventoryGUIInstance<Container>, Integer, @Nullable ItemStack> item, GUIClickInInteractiveAreaEvent<Container> onClick){
        /**
         * スロットの位置をエリア上の位置に変換します。
         * @param slot インベントリGUI上での位置
         * @return エリア上の位置
         */
        public int getIndex(int slot){
            int index = 0;
            for(int i = 0;i<slot;i++){
                if(predicate.predicate(new SlotInfo(i))) index++;
            }
            return index;
        }
    }

    @Nullable PlayerAreaEditor<Container> playerAreaEditor = null;
    List<GUIFixedItem> fixedItems = new ArrayList<>();
    List<GUIStaticItem<Container>> staticItems = new ArrayList<>();
    List<GUIDynamicItem<Container>> dynamicItems = new ArrayList<>();
    List<GUIAreaItem<Container>> areaItems = new ArrayList<>();
    List<GUIInteractiveAreaItem<Container>> interactiveItems = new ArrayList<>();
    final int lines;
    final Component title;
    @Nullable ExecutionScenario<?> scenario = null;

    public InventoryGUI(String title, int lines){
        this.title = Component.text(title);
        this.lines = lines;
    }

    public InventoryGUI(Component title, int lines){
        this.title = title;
        this.lines = lines;
    }

    /**
     * プレイヤーのインベントリを操作しようとすると呼び出されるコールバックを追加します。
     * @param editor プレイヤーインベントリのエディタ
     * @return インベントリGUI定義
     */
    public InventoryGUI<Container> playerAreaEditor(@Nullable PlayerAreaEditor<Container> editor){
        this.playerAreaEditor = editor;
        return this;
    }

    /**
     * クリックできない要素を追加します。
     * この要素をクリックしても何も起こらず、画面も更新されません。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI<Container> pushContent(GUIFixedItem item){
        this.fixedItems.add(item);
        return this;
    }

    /**
     * クリックできる静的な要素を追加します。
     * この要素をクリックすると、紐づいたアクションが実行され、画面が更新されます。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI<Container> pushContent(GUIStaticItem<Container> item){
        this.staticItems.add(item);
        return this;
    }

    /**
     * クリックできる動的な要素を追加します。
     * この要素をクリックすると、紐づいたアクションが実行され、画面が更新されます。
     * この要素は画面が更新されるたびにアイテムを変更できます。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI<Container> pushContent(GUIDynamicItem<Container> item){
        this.dynamicItems.add(item);
        return this;
    }

    /**
     * クリックできる動的なコレクションの要素を追加します。
     * この要素をクリックすると、紐づいたアクションが実行され、画面が更新されます。
     * クリック操作によって関連するコレクションを変更する場合、GUIを開いている間の並行したコレクションの操作は回避されるべきです。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI<Container> pushContent(GUIAreaItem<Container> item){
        this.areaItems.add(item);
        return this;
    }

    /**
     * クリックおよびアイテムの持ち込み、持ち出しができる動的なコレクションの要素を追加します。
     * この要素をクリックすると、紐づいたアクションが実行され、画面が更新されます。
     * クリック操作によって関連するコレクションを変更する場合、GUIを開いている間の並行したコレクションの操作は回避されるべきです。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI<Container> pushContent(GUIInteractiveAreaItem<Container> item){
        this.interactiveItems.add(item);
        return this;
    }

    public InventoryGUI<Container> withScenario(ExecutionScenario<?> chain){
        this.scenario = chain;
        return this;
    }

    public AbstractExecution<Container> execute(Player player, Container container){
        return new InventoryGUIInstance<>(this, player, container);
    }
}
