package jp.dreamingpig.dollyMC.utils.execution.actionEvent;


import jp.dreamingpig.dollyMC.utils.execution.ContainerConstructor;
import jp.dreamingpig.dollyMC.utils.execution.ExecutionScenario;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ActionEvent<Container> {
    public interface ChatAction<Container>{
        boolean onChat(ActionEventInstance<Container> instance, Component chat);
    }
    public interface EntityAction<Container>{
        boolean onClickEntity(ActionEventInstance<Container> instance, Entity entity, ClickType clickType);
    }
    public interface BlockAction<Container>{
        boolean onClickBlock(ActionEventInstance<Container> instance, Block block, BlockFace blockFace, ClickType clickType);
    }
    public interface AirAction<Container>{
        boolean onClickAir(ActionEventInstance<Container> instance, ClickType clickType);
    }

    @Getter @Setter(AccessLevel.PRIVATE)
    private ContainerConstructor<Container> constructor;
    @Nullable ChatAction<Container> onChat = null;
    @Nullable BlockAction<Container> onClickBlock = null;
    @Nullable EntityAction<Container> onClickEntity = null;
    @Nullable AirAction<Container> onClickAir = null;
    @Nullable Consumer<ActionEventInstance<Container>> onActivated = null;
    @Nullable ExecutionScenario<?> scenario = null;

    public ActionEvent(ContainerConstructor<Container> constructor){
        this.constructor = constructor;
    }

    /**
     * イベントが有効化されたときに実行する処理を設定します。
     */
    public ActionEvent<Container> activatedAction(Consumer<ActionEventInstance<Container>> onActivated){
        this.onActivated = onActivated;
        return this;
    }

    /**
     * チャットによるテキスト入力を受けて処理を差し込みます。
     * 関数がtrueを返す場合、チャットは全体に送信されません。
     * @param onChat
     * @return
     */
    public ActionEvent<Container> withChat(ChatAction<Container> onChat){
        this.onChat = onChat;
        return this;
    }

    /**
     * ブロックに対するインタラクトに動作をフックします。
     * 関数がtrueを返す場合、本来の動作をキャンセルします。
     * {@link ClickType}は左右のクリックとそれぞれのシフト同時押しの4つのいずれかを受け取ります。
     */
    public ActionEvent<Container> withClickBlock(BlockAction<Container> onClickBlock){
        this.onClickBlock = onClickBlock;
        return this;
    }

    /**
     * エンティティに対するインタラクトに動作をフックします。
     * 関数がtrueを返す場合、本来の動作をキャンセルします。
     * {@link ClickType}は左右のクリックとそれぞれのシフト同時押しの4つのいずれかを受け取ります。
     */
    public ActionEvent<Container> withClickEntity(EntityAction<Container> onClickEntity){
        this.onClickEntity = onClickEntity;
        return this;
    }

    /**
     * 空気に対するインタラクトに動作をフックします。
     * 関数がtrueを返す場合、本来の動作をキャンセルします。
     * {@link ClickType}は右クリックとシフト右クリックの2つのいずれかを受け取ります。
     */
    public ActionEvent<Container> withClickAir(AirAction<Container> onClickAir){
        this.onClickAir = onClickAir;
        return this;
    }

    public ActionEvent<Container> withScenario(ExecutionScenario<?> chain){
        this.scenario = chain;
        return this;
    }
}
