package jp.dreamingpig.dollyMC;

import jp.dreamingpig.dollyMC.utils.execution.inventoryGui.InventoryGUIListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DollyMC extends JavaPlugin {
    private static DollyMC plugin;
    public static DollyMC getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        //イベントリスナの登録
        new InventoryGUIListener();
    }

    @Override
    public void onDisable() {
    }
}
