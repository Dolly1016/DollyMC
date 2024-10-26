package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import jp.dreamingpig.dollyMC.utils.execution.CloseRule;
import jp.dreamingpig.dollyMC.utils.execution.IExecutable;
import jp.dreamingpig.dollyMC.utils.execution.IExecutionHandler;
import jp.dreamingpig.dollyMC.utils.execution.IExecution;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * クリック操作ができるインベントリGUIの定義です。
 * この定義をもとに{@link InventoryGUIInstance}が生成されます。
 */
public class InventoryGUI extends IExecutable {
    public interface PlayerAreaEditor{
        boolean onEdit(InventoryGUIInstance instance, int slot);
    }
    public interface GUIClickEvent{
        void onClick(InventoryGUIInstance instance);
    }
    public interface GUIClickInAreaEvent{
        void onClick(InventoryGUIInstance instance, int index);
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
    public record GUIStaticItem(int slot, ItemStack item, GUIClickEvent onClick){}
    public record GUIDynamicItem(int slot, Function<InventoryGUIInstance, ItemStack> item, GUIClickEvent onClick){}
    public record GUIAreaItem(SlotPredicate predicate, BiFunction<InventoryGUIInstance, Integer, @Nullable ItemStack> item, GUIClickInAreaEvent onClick){
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

    @Nullable PlayerAreaEditor playerAreaEditor = null;
    List<GUIFixedItem> fixedItems = new ArrayList<>();
    List<GUIStaticItem> staticItems = new ArrayList<>();
    List<GUIDynamicItem> dynamicItems = new ArrayList<>();
    List<GUIAreaItem> areaItems = new ArrayList<>();
    final int lines;
    final Component title;
    CloseRule closeRule = CloseRule.TREAT_INTERRUPT;

    public InventoryGUI(String title, int lines){
        this.title = Component.text(title);
        this.lines = lines;
    }

    public InventoryGUI(Component title, int lines){
        this.title = title;
        this.lines = lines;
    }

    public InventoryGUI closeRule(CloseRule rule){
        this.closeRule = rule;
        return this;
    }

    public InventoryGUI playerAreaEditor(@Nullable PlayerAreaEditor editor){
        this.playerAreaEditor = editor;
        return this;
    }

    /**
     * クリックできない要素を追加します。
     * この要素をクリックしても何も起こらず、画面も更新されません。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI pushContent(GUIFixedItem item){
        this.fixedItems.add(item);
        return this;
    }

    /**
     * クリックできる静的な要素を追加します。
     * この要素をクリックすると、紐づいたアクションが実行され、画面が更新されます。
     * @param item 要素
     * @return インベントリGUI定義
     */
    public InventoryGUI pushContent(GUIStaticItem item){
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
    public InventoryGUI pushContent(GUIDynamicItem item){
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
    public InventoryGUI pushContent(GUIAreaItem item){
        this.areaItems.add(item);
        return this;
    }

    @Override
    protected IExecution runImpl(@Nullable IExecutionHandler handler, @Nullable Player player) {
        return new InventoryGUIInstance(player, this, handler);
    }
}
