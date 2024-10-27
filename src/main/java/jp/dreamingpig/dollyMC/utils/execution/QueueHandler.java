package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class QueueHandler extends IExecutionHandler {
    Queue<IExecutable> queued;

    public QueueHandler(@Nullable IExecutionHandler handler, Player player){
        super(handler, player);
        this.queued = new ArrayDeque<>();
    }

    @Override
    public boolean push(IExecutable executable){
        executable.setHandler(this);
        queued.add(executable);
        return true;
    }

    @Override
    @Nullable
    protected IExecution runImpl(@Nullable IExecutionHandler handler, Player player){
        if(handler != getHandler()) throw new IllegalArgumentException("Unmatched handler has been received.");

        return null;
    }
}
