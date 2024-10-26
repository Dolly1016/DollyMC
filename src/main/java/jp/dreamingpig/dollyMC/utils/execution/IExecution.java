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
     * 実行を終了します。
     */
    default void terminate(boolean complete){
        if(!getProcess().isClosed()) {
            getProcess().close(!complete);

            //現在の実行可能要素であった場合はポップして適宜実行を続ける。
            if(getHandler() != null && getHandler().peekExecutable() == getProcess()) getHandler().pop();
        }
    }
}
