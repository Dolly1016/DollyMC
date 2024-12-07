package jp.dreamingpig.dollyMC;

import jp.dreamingpig.dollyMC.player.OnlinePlayerListener;
import jp.dreamingpig.dollyMC.utils.execution.actionEvent.ActionEventListener;
import jp.dreamingpig.dollyMC.utils.execution.inventoryGui.InventoryGUIListener;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.awt.*;

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

        var reflections = new Reflections(new ConfigurationBuilder()
                .setClassLoaders(new ClassLoader[]{ DollyMC.class.getClassLoader() })
                .setScanners(Scanners.TypesAnnotated)
        );
        for(var t : reflections.getTypesAnnotatedWith(Deprecated.class)){
            Bukkit.getLogger().info(t.getName());
        }
    }

    @Override
    public void onDisable() {
    }
}
