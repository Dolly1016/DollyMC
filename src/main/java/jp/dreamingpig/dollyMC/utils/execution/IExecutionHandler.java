package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IExecutionHandler extends IExecutable {

    abstract public Player getPlayer();

    /**
     * 新たな実行可能要素を追加し、実行します。
     * @param executable 実行可能要素
     */
    abstract public @Nullable IExecution push(IExecutable executable);

    /**
     * 現在の実行可能要素を取得します。
     * @return 実行可能要素
     */
    abstract protected @Nullable IExecutable peekExecutable();

    /**
     * 現在の実行可能要素を完了させたうえで削除し、直前の実行可能要素を再実行します。
     * @return 実行がある場合、実行
     */
    abstract public @Nullable IExecution pop();

    /**
     * このハンドラを実行します。中断されていた場合は再度実行します。
     */
    abstract public @Nullable IExecution resume();

    abstract public @NotNull IExecutionHandler getRoot();
}
