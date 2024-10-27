package jp.dreamingpig.dollyMC.utils.execution.actionEvent;


import jp.dreamingpig.dollyMC.utils.execution.IExecutable;
import jp.dreamingpig.dollyMC.utils.execution.IExecution;
import jp.dreamingpig.dollyMC.utils.execution.IExecutionHandler;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ActionEvent extends IExecutable {
    public interface ChatAction{
        boolean onChat(ActionEventInstance instance, Component chat);
    }
    public interface EntityAction{
        boolean onClickEntity(ActionEventInstance instance, Entity entity, ClickType clickType);
    }
    public interface BlockAction{
        boolean onClickBlock(ActionEventInstance instance, Block block, BlockFace blockFace, ClickType clickType);
    }
    public interface AirAction{
        boolean onClickAir(ActionEventInstance instance, ClickType clickType);
    }

    @Nullable ChatAction onChat = null;
    @Nullable BlockAction onClickBlock = null;
    @Nullable EntityAction onClickEntity = null;
    @Nullable AirAction onClickAir = null;
    @Nullable Consumer<ActionEventInstance> onActivated = null;

    public ActionEvent(){}

    /**
     * イベントが有効化されたときに実行する処理を設定します。
     */
    public ActionEvent activatedAction(Consumer<ActionEventInstance> onActivated){
        this.onActivated = onActivated;
        return this;
    }

    /**
     * チャットによるテキスト入力を受けて処理を差し込みます。
     * 関数がtrueを返す場合、チャットは全体に送信されません。
     * @param onChat
     * @return
     */
    public ActionEvent withChat(ChatAction onChat){
        this.onChat = onChat;
        return this;
    }

    /**
     * ブロックに対するインタラクトに動作をフックします。
     * 関数がtrueを返す場合、本来の動作をキャンセルします。
     * {@link ClickType}は左右のクリックとそれぞれのシフト同時押しの4つのいずれかを受け取ります。
     */
    public ActionEvent withClickBlock(BlockAction onClickBlock){
        this.onClickBlock = onClickBlock;
        return this;
    }

    /**
     * エンティティに対するインタラクトに動作をフックします。
     * 関数がtrueを返す場合、本来の動作をキャンセルします。
     * {@link ClickType}は左右のクリックとそれぞれのシフト同時押しの4つのいずれかを受け取ります。
     */
    public ActionEvent withClickEntity(EntityAction onClickEntity){
        this.onClickEntity = onClickEntity;
        return this;
    }

    /**
     * 空気に対するインタラクトに動作をフックします。
     * 関数がtrueを返す場合、本来の動作をキャンセルします。
     * {@link ClickType}は右クリックとシフト右クリックの2つのいずれかを受け取ります。
     */
    public ActionEvent withClickAir(AirAction onClickAir){
        this.onClickAir = onClickAir;
        return this;
    }

    @Override
    protected IExecution runImpl(@Nullable IExecutionHandler handler, @Nullable Player player) {
        return new ActionEventInstance(player, this, handler);
    }
}
