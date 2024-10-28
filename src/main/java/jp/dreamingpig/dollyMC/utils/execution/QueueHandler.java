package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class QueueHandler extends IExecutionHandler {
    Queue<IExecutable> queued;
    boolean cancelImmediately;

    public QueueHandler(@Nullable IExecutionHandler handler, Player player, boolean cancelImmediately){
        super(handler, player);
        this.queued = new ArrayDeque<>();
        this.cancelImmediately = cancelImmediately;
    }

    @Override
    public boolean push(IExecutable executable){
        executable.setHandler(this);
        queued.add(executable);
        return true;
    }

    void onClosed(IExecutable executable){
        if(cancelImmediately){
            close(true);
        }
    }

    @Override
    @Nullable
    protected IExecution runImpl(@Nullable IExecutionHandler handler, Player player){
        if(handler != getHandler()) throw new IllegalArgumentException("Unmatched handler has been received.");

        if(isClosed()) return null;
        while(!queued.isEmpty()){
            var peeked = queued.peek();
            if(peeked.isClosed()){
                queued.remove();
            }else{
                return peeked.run(this, getPlayer());
            }
        }
        return null;
    }
}
