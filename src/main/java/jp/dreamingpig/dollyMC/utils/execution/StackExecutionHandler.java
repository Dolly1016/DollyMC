package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class StackExecutionHandler extends IExecutionHandler {
    final private Stack<IExecutable> processStack;

    /**
     * 実行可能要素を1つ含むハンドラを生成します。
     * @param handler 親ハンドラ
     * @param player プレイヤー
     * @param executable 実行可能要素
     */
    public StackExecutionHandler(@Nullable IExecutionHandler handler, Player player, IExecutable executable){
        super(handler, player);
        this.processStack = new Stack<>();
        push(executable);
    }

    @Override
    public boolean push(IExecutable executable){
        executable.setHandler(this);
        processStack.push(executable);
        return true;
    }

    @Override
    @Nullable
    protected IExecution runImpl(@Nullable IExecutionHandler handler, Player player){
        if(handler != getHandler()) throw new IllegalArgumentException("Unmatched handler has been received.");

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
}
