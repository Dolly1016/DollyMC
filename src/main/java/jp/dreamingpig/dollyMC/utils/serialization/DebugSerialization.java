package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.Reader;
import java.io.StringReader;

public class DebugSerialization implements ISerialization {
    final private FileConfiguration memory;

    /**
     * 空のデータ構造を生成します。
     */
    public DebugSerialization(){
        memory = YamlConfiguration.loadConfiguration(Reader.nullReader());
    }

    /**
     * ファイルを読み込むシチュエーションをデバッグできます。
     * 実際のファイルの代わりに、ファイルの中身に含まれると想定されたテキストを渡します。
     * @param yamlText データを表すテキスト
     */
    public DebugSerialization(String yamlText)
    {
        memory = YamlConfiguration.loadConfiguration(new StringReader(yamlText));
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
