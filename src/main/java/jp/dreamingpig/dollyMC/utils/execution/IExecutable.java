package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class IExecutable {
    private boolean isClosed = false;
    private boolean isCanceled = false;

    public boolean isClosed(){
        return isClosed;
    }
    void close(boolean isCanceled){
        isClosed = true;
        this.isCanceled = isCanceled;
    }

    /**
     * 実行可能要素が完了したかどうか調べます。
     * 実行中、未実行、再実行待ちの場合はfalseが返ります。
     */
    public boolean isCompleted(){
        return isClosed && !isCanceled;
    }

    /**
     * 実行可能要素がキャンセルされたかどうか調べます。
     * 実行中、未実行、再実行待ちの場合はfalseが返ります。
     */
    public boolean isCanceled(){
        return isClosed && isCanceled;
    }

    /**
     * プロセスを実行します。
     */
    protected abstract @Nullable IExecution runImpl(@Nullable IExecutionHandler handler, Player player);

    /**
     * この実行可能要素を実行します。
     * 正しい場所で実行されている必要があるため、外部に公開していません。
     */
    @Nullable IExecution run(@Nullable IExecutionHandler handler, Player player){
        return runImpl(handler, player);
    }

}
