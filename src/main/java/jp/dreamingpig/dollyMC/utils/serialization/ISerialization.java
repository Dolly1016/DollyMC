package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.file.FileConfiguration;

import java.awt.image.MemoryImageSource;

interface ISerialization {
    FileConfiguration getConfig();
    void saveConfig();
}
