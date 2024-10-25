package jp.dreamingpig.dollyMC;

import org.bukkit.plugin.java.JavaPlugin;

public final class DollyMC extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        /*
        var structure =DSerialization.openStructure(this, "test");
        var abc = structure.getStructureList("list");
        var content1 = abc.add();
        var intEntry1 = content1.getInt("a", 10);
        intEntry1.set(20);

        var content2 = abc.add();
        var intEntry2 = content2.getInt("a", 10);
        intEntry2.set(30);

        structure.save();
        */
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
