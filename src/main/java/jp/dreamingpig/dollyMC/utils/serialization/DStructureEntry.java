package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class DStructureEntry {
    private DSerializationWrapper wrapper;

    DStructureEntry(DSerializationWrapper wrapper){
        this.wrapper = wrapper;
    }

    public DPrimitiveEntry<Integer> getInt(String id, int defaultValue){
        return new DIntegerEntry(wrapper, id, defaultValue);
    }

    public List<Integer> getIntList(String id){
        return wrapper.getIntList(id);
    }

    public DPrimitiveEntry<Long> getLong(String id, long defaultValue){
        return new DLongEntry(wrapper, id, defaultValue);
    }

    public List<Long> getLongList(String id){
        return wrapper.getLongList(id);
    }

    public DPrimitiveEntry<Double> getDouble(String id, double defaultValue){
        return new DDoubleEntry(wrapper, id, defaultValue);
    }

    public List<Double> getDoubleList(String id){
        return wrapper.getDoubleList(id);
    }

    public DPrimitiveEntry<String> getString(String id, String defaultValue){
        return new DStringEntry(wrapper, id, defaultValue);
    }

    public List<String> getStringList(String id){
        return wrapper.getStringList(id);
    }

    public DPrimitiveEntry<ItemStack> getString(String id, @Nullable ItemStack defaultValue){
        return new DItemStackEntry(wrapper, id, defaultValue);
    }

    public List<ItemStack> getItemStackList(String id){
        return wrapper.getItemStackList(id);
    }

    public DPrimitiveEntry<BigInteger> getBigInteger(String id, BigInteger defaultValue){
        return new DBigIntegerEntry(wrapper, id, defaultValue);
    }

    public DPrimitiveEntry<UUID> getUUID(String id, UUID defaultValue){
        return new DUUIDEntry(wrapper, id, defaultValue);
    }

    public DStructureEntry getStructure(String id){
        return new DStructureEntry(wrapper.editMap(id));
    }

    public DStructureListEntry getStructureList(String id){
        return new DStructureListEntry(wrapper.getSerialization(), wrapper.editList(id));
    }

     public boolean contains(String id) {
        return wrapper.contains(id);
     }

    public void save(){
        wrapper.save();
    }

    public ConfigurationSection getSection() {
        return wrapper.getSection();
    }

    /**
     * ディスク上に保存されるデータ構造を生成します。
     * 既に保存されているデータがある場合、そのデータを読み出せます。
     * @param plugin データ構造を使用するプラグイン
     * @param fileName ディスク上に保存する際のファイル名(拡張子不要)
     * @return ディスク上に保存可能なデータ構造
     */
    public static DStructureEntry openStructure(@NotNull Plugin plugin, @NotNull String fileName){
        return new DStructureEntry(new SectionWrapper(new DSerialization(plugin, fileName)));
    }

    /**
     * デバッグ用のデータ構造を生成します。
     * ディスク上に保存することはできません。
     * @return デバッグ用のデータ構造
     */
    public static DStructureEntry debugStructure(DebugSerialization serialization){
        return new DStructureEntry(new SectionWrapper(serialization));
    }
}
