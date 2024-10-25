package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.Reader;

public class DebugSerialization implements ISerialization {
    final private FileConfiguration memory;

    public DebugSerialization(){
        memory = YamlConfiguration.loadConfiguration(Reader.nullReader());
    }

    @Override
    public FileConfiguration getConfig() {
        return memory;
    }

    @Override
    public void saveConfig() {
    }

    @Override
    public String toString(){
        return memory.saveToString();
    }
}
