package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface IExecution {
    /**
     * 実行可能要素を取得します。
     */
    IExecutable getProcess();

    /**
     * 実行ハンドラを取得します。
     * @return 実行ハンドラ
     */
    @Nullable IExecutionHandler getHandler();

    /**
     * 実行に関連するプレイヤーを取得します。
     * @return プレイヤー
     */
    Player getPlayer();

    /**
     * 実行が有効な場合trueを返します。
     */
    boolean isActive();

    /**
     * 実行を中止します。
     * 実行の種類によって、実行可能要素は中断したりキャンセル、完了します。
     */
    void suspend();

    /**
     * 紐づく実行可能要素を閉じ、直前の実行可能要素に引き返します。
     * この実行が現在のハンドラの有効な実行である場合にのみ機能します。
     */
    default boolean turnBack(boolean complete){
        if(!getProcess().isClosed() && getHandler() != null && getHandler().getLastExecutable() == getProcess()) {
            getProcess().close(!complete);
            suspend();
            return true;
        }
        return false;
    }
}
