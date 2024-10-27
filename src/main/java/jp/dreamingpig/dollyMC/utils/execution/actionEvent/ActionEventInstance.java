package jp.dreamingpig.dollyMC.utils.execution.actionEvent;


import jp.dreamingpig.dollyMC.utils.execution.AbstractExecution;
import jp.dreamingpig.dollyMC.utils.execution.IExecutable;
import jp.dreamingpig.dollyMC.utils.execution.IExecutionHandler;
import org.bukkit.entity.Player;

public class ActionEventInstance extends AbstractExecution {
    ActionEvent myEvent;

    ActionEventInstance(Player player, ActionEvent definition, IExecutionHandler handler) {
        super(player, handler);

        myEvent = definition;

        ActionEventListener.registerInventoryGUIInstance(player.getUniqueId(), this);
    }

    void onUnregistered(){
        inactivate();
    }

    @Override
    public IExecutable getProcess(){
        return myEvent;
    }

    /**
     * 実行を中止します。
     * 実行の種類によって、中断されたりキャンセルあるいは正常に実行が完了します。
     */
    @Override
    public void suspend() {
        if (isActive()) ActionEventListener.unregister(getPlayer().getUniqueId());
    }
}