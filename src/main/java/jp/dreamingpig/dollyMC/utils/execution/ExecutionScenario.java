package jp.dreamingpig.dollyMC.utils.execution;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExecutionScenario<Container> {
    @Getter
    Container container;
    BiConsumer<ExecutionScenario<Container>, @Nullable Runnable> onUpdate;
    @Nullable  Consumer<ExecutionScenario<Container>> firstUpdate;

    public ExecutionScenario(Container container, @Nullable Consumer<ExecutionScenario<Container>> firstUpdate, BiConsumer<ExecutionScenario<Container>, Runnable> onUpdate){
        this.container = container;
        this.firstUpdate = firstUpdate;
        this.onUpdate = onUpdate;
    }

    void onSuspendExecution(@Nullable Runnable callback){
        onUpdate.accept(this, callback);
    }

    public void run(){
        if(firstUpdate != null)
            firstUpdate.accept(this);
        else
            onUpdate.accept(this,null);
    }
}
