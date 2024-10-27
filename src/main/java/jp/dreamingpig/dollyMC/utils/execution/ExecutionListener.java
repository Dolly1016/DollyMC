package jp.dreamingpig.dollyMC.utils.execution;

import jp.dreamingpig.dollyMC.utils.execution.actionEvent.ActionEventListener;
import jp.dreamingpig.dollyMC.utils.execution.inventoryGui.InventoryGUIListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ExecutionListener {
    static public void ResetAllExecution(UUID player){
        InventoryGUIListener.unregister(player);
        ActionEventListener.unregister(player);
    }
}
