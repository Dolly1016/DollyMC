package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IExecutionHandler extends IExecutable {
    final private Player player;

    protected IExecutionHandler(IExecutionHandler handler, Player player){
        this.player = player;
        setHandler(handler);
    }

    public Player getPlayer(){
        return player;
    }

    //ルートハンドラは最後の実行を記憶する
    private IExecution lastExecution = null;
    IExecution getLastExecution() {
        return getRoot().lastExecution;
    }
    protected void setLastExecution(IExecution execution) {
        getRoot().lastExecution = lastExecution;
    }

    protected IExecutable getLastExecutable(){
        var lastExecution = getRoot().lastExecution;
        if(lastExecution == null) return null;
        return lastExecution.getProcess();
    }

    void onClosed(IExecutable executable){
    }

    /**
     * 新たな実行可能要素を追加します。
     * @param executable 実行可能要素
     */
    abstract public boolean push(IExecutable executable);

    /**
     * このハンドラを実行します。中断されていた場合は再度実行します。
     */
    public @Nullable IExecution resume(){
        if(getHandler() != null) return getHandler().resume();
        else {
            var execution = run(null, player);
            setLastExecution(execution);
            return execution;
        }
    }

    public @NotNull IExecutionHandler getRoot(){
        if(getHandler() == null) return this;
        return getHandler().getRoot();
    }
}
