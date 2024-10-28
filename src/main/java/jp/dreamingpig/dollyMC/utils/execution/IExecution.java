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
     * 新たな実行可能要素を追加し、実行します。
     * この実行は中断されます。
     * @param executable 追加する実行可能要素
     * @return 追加が成功した場合、trueが返ります。
     */
    default boolean deepen(IExecutable executable){
        if(getHandler() == null) return false;
        suspend();
        getHandler().push(executable);
        getHandler().resume();
        return true;
    }

    /**
     * この実行可能要素を別の実行可能要素に挿げ替えます。
     * この実行可能要素の完了状態を指定できます。
     * @param executable 挿げ替える実行可能要素
     * @param complete この実行可能要素の完了状態
     * @return 挿げ替えが成功した場合trueが返ります。
     */
    default boolean step(@Nullable IExecutable executable, boolean complete){
        if(executable == null) return step(complete);
        boolean result = deepen(executable);
        getProcess().close(!complete);
        return result;
    }

    /**
     * この実行可能要素を終了して、ハンドラを実行します。
     */
    default boolean step(boolean complete){
        if(getHandler() == null) return false;
        getProcess().close(!complete);
        suspend();
        getHandler().resume();
        return true;
    }
}
