package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * ディスク上に保存されるデータ構造
 * @author Dolly
 * @version 1.0.0
 */
class DSerialization implements ISerialization{
    @Nullable
    private FileConfiguration config = null;
    @NotNull
    private final File configFile;
    @NotNull
    private final String file;
    @NotNull
    private final Plugin plugin;

    /**
     * ディスク上に保存されるデータ構造を生成します。
     * 既に保存されているデータがある場合、そのデータを読み出せます。
     * @param plugin データ構造を使用するプラグイン
     * @param fileName ディスク上に保存する際のファイル名(拡張子不要) フォルダの区切り文字には半角スラッシュを使用できます。
     */
    public DSerialization(@NotNull Plugin plugin, @NotNull String fileName) {
        this.plugin = plugin;
        this.file = fileName.replace('/', File.separatorChar) + ".yml";
        configFile = new File(plugin.getDataFolder(), file);
    }

    /*
    private void saveDefault() {
        if (!configFile.exists()) plugin.saveResource(file, false);
    }
    */

    private void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if (defConfigStream == null) return;

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    @Override
    public FileConfiguration getConfig() {
        if (config == null) reloadConfig();
        return config;
    }

    @Override
    public void saveConfig() {
        if (config == null) return;
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
}
