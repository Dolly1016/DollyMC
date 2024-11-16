package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DSerializationWrapper {
    boolean contains(String id);
    Iterable<String> getKeys();

    int getInt(String id);
    void setInt(String id, int value);
    List<Integer> getIntList(String id);

    long getLong(String id);
    void setLong(String id, long value);
    List<Long> getLongList(String id);

    double getDouble(String id);
    void setDouble(String id, double value);
    List<Double> getDoubleList(String id);

    String getString(String id);
    void setString(String id, String value);
    List<String> getStringList(String id);

    @Nullable
    ItemStack getItemStack(String id);
    void setItemStack(String id, ItemStack value);
    List<ItemStack> getItemStackList(String id);

    DSerializationWrapper editMap(String id);
    List<Map<String, ?>> editList(String id);

    void save();

    ISerialization getSerialization();
}
