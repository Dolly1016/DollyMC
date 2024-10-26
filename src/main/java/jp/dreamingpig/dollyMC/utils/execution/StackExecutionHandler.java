package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class StackExecutionHandler extends IExecutionHandler {
    final private Stack<IExecutable> processStack;
    final @Nullable private IExecutionHandler myHandler;
    final private Player player;

    /**
     * 実行可能要素を1つ含むハンドラを生成します。
     * @param handler 親ハンドラ
     * @param player プレイヤー
     * @param executable 実行可能要素
     */
    public StackExecutionHandler(@Nullable IExecutionHandler handler, Player player, IExecutable executable){
        this.myHandler = handler;
        this.player = player;
        this.processStack = new Stack<>();
        processStack.push(executable);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * 新たな実行可能要素を追加し、実行します。
     * @param executable 追加する実行可能要素
     */
    @Override
    @Nullable
    public IExecution push(IExecutable executable){
        processStack.push(executable);
        return resume();
    }

    @Override
    protected @Nullable IExecutable peekExecutable() {
        if(processStack.isEmpty()) return null;
        return processStack.peek();
    }

    @Override
    @Nullable
    public IExecution pop(){
        if(processStack.isEmpty())return null;
        processStack.pop().close(false);
        return resume();
    }

    @Override
    @Nullable
    protected IExecution runImpl(@Nullable IExecutionHandler handler, Player player){
        if(handler != myHandler) throw new IllegalArgumentException("Unmatched handler has been received.");

        while(!processStack.isEmpty()) {
            var peeked = processStack.peek();

            //完了済みの実行可能要素は削除する。
            if(peeked.isClosed()){
                processStack.pop();
                continue;
            }

            var instance = peeked.run(this, player);
            if (instance != null) return instance;

            //実行できなくなった実行可能要素も削除する。
            processStack.pop();
        }
        return null;
    }

    /**
     * 親のrunメソッドは現在の実行可能要素を適切に実行します。
     * @return 現在の実行
     */
    @Override
    @Nullable
    public IExecution resume(){
        if(myHandler != null) return myHandler.resume();
        else return run(null, player);
    }

    @Override
    public @NotNull IExecutionHandler getRoot() {
        if(myHandler == null) return this;
        return myHandler.getRoot();
    }
}
