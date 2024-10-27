package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

public class BranchHandler extends IExecutionHandler{
    final private IExecutable executable;
    @Nullable private IExecutable resultExecutable = null;
    final private Function<IExecutionHandler, IExecutable> thenExecutable;
    final private Function<IExecutionHandler, IExecutable> cancelExecutable;

    public BranchHandler(@Nullable IExecutionHandler handler, Player player, IExecutable executable, Function<IExecutionHandler, IExecutable> thenExecutable, Function<IExecutionHandler, IExecutable> cancelExecutable){
        super(handler, player);
        this.executable = executable;
        this.thenExecutable = thenExecutable;
        this.cancelExecutable = cancelExecutable;

        executable.setHandler(this);
    }

    @Override
    public boolean push(IExecutable executable) {
        return false;
    }

    @Override
    protected @Nullable IExecution runImpl(@Nullable IExecutionHandler handler, Player player) {
        if(handler != getHandler()) throw new IllegalArgumentException("Unmatched handler has been received.");

        if(executable.isClosed()){
            if(resultExecutable == null) {
                resultExecutable = (executable.isCompleted() ? thenExecutable : cancelExecutable).apply(this);
            }

            if(resultExecutable == null) throw new IllegalArgumentException("Failed generation of result executable.");

            return resultExecutable.run(this, player);
        }else{
            return executable.run(this, player);
        }
    }
}
