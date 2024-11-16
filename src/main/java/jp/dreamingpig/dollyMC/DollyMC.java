package jp.dreamingpig.dollyMC;

import jp.dreamingpig.dollyMC.utils.execution.actionEvent.ActionEventListener;
import jp.dreamingpig.dollyMC.utils.execution.inventoryGui.InventoryGUIListener;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DollyMC extends JavaPlugin {
    private static DollyMC plugin;
    public static DollyMC getPlugin() {
        return plugin;
    }
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
    }

    @Override
    public void onDisable() {
    }
}
