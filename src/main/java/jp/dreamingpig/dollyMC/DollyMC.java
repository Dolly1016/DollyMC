package jp.dreamingpig.dollyMC;

import jp.dreamingpig.dollyMC.player.OnlinePlayerListener;
import jp.dreamingpig.dollyMC.utils.execution.actionEvent.ActionEventListener;
import jp.dreamingpig.dollyMC.utils.execution.inventoryGui.InventoryGUIListener;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DollyMC extends JavaPlugin {
    @Getter
    private static DollyMC plugin;
    private static DStructureEntry optionEntry;
    public static DStructureEntry getOption(){
        return optionEntry;
    }

    @Override
    public void onEnable() {
        plugin = this;
        optionEntry = DStructureEntry.openStructure(this, "configuration");

        //イベントリスナの登録
        new InventoryGUIListener();
        new ActionEventListener();
        new OnlinePlayerListener();

        //デバッグ用
        new DebugListener();
    }

    @Override
    public void onDisable() {
    }
}
