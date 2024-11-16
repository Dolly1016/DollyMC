package jp.dreamingpig.dollyMC.utils.execution;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 実行を表します。
 * @param <Container> 実行の状態を保存するコンテナ
 */
public abstract class AbstractExecution<Container> {
    static Map<UUID, AbstractExecution<?>> executionMap = new HashMap<>();
    @Nullable
    static public AbstractExecution<?> getExecution(@NotNull UUID player){
        return executionMap.get(player);
    }
    static private void registerExecution(@NotNull UUID player, @NotNull AbstractExecution<?> execution) {
        unregisterExecution(player, () -> executionMap.put(player, execution));
    }
    static public void unregisterExecution(@NotNull UUID player, @Nullable Runnable callback){
        var current = executionMap.get(player);
        if(current != null){
            current.suspend(callback);
            executionMap.remove(player);
        }else if(callback != null){
            callback.run();
        }
    }

    @Getter @Setter(AccessLevel.PRIVATE)
    private Player player;
    @Getter @Setter(AccessLevel.PRIVATE)
    private Container container;

    public abstract ExecutionScenario<?> getScenario();

    protected AbstractExecution(Player player, Container container){
        setPlayer(player);
        setContainer(container);
        registerExecution(player.getUniqueId(), this);
    }

    /**
     * 実行が中断されたときに一度だけ呼び出されます。
     * この処理を上書きする場合は、callbackを必ず実行してください。
     * @param callback 中断後に実行する処理
     */
    protected void onSuspend(@Nullable Runnable callback){
        //コールバックを覚えておいて、何か確認用のステップを挟んでもいいかも。
        if(callback != null) callback.run();
    }

    /**
     * 実行を中断するときに呼び出します。
     * @param callback 中断後に実行する処理
     */
    void suspend(@Nullable Runnable callback){
        var chain = getScenario();
        if(chain != null){
            onSuspend(()->chain.onSuspendExecution(callback));
        }else {
            onSuspend(callback);
        }
    }

    public boolean trySuspend(@Nullable Runnable callback){
        var current = getExecution(getPlayer().getUniqueId());
        if(current == this){
            unregisterExecution(getPlayer().getUniqueId(), callback);
            return true;
        }
        return false;
    }
}
