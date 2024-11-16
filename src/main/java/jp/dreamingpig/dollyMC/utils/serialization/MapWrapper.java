package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapWrapper implements DSerializationWrapper{
    private ISerialization serialization;
    private Map<String, Object> myMap;

    public MapWrapper(ISerialization serialization, Map<String, Object> map){
        this.serialization = serialization;
        this.myMap = map;
    }

    @Override
    public Iterable<String> getKeys(){
        return this.myMap.keySet();
    }

    @Override
    public boolean contains(String id){
        return myMap.containsKey(id);
    }

    private <T> List<T> getList(String id) {
        if (myMap.containsKey(id)) {
            var list = myMap.get(id);
            if (list instanceof List<?> correctList) return (List<T>)correctList;
        }
        List<T> newList = new ArrayList<>();
        myMap.put(id, newList);
        return newList;
    }

    @Override
    public int getInt(String id){
        if(myMap.get(id) instanceof Integer num) return num;
        return 0;
    }
    @Override
    public void setInt(String id, int value){
        myMap.put(id, value);
    }
    @Override
    public List<Integer> getIntList(String id) {
        return this.<Integer>getList(id);
    }

    @Override
    public long getLong(String id) {
        if(myMap.get(id) instanceof Long num) return num;
        return 0;
    }

    @Override
    public void setLong(String id, long value) {
        myMap.put(id, value);
    }

    @Override
    public List<Long> getLongList(String id) {
        return this.<Long>getList(id);
    }

    @Override
    public double getDouble(String id) {
        if(myMap.get(id) instanceof Double num) return num;
        return 0;
    }

    @Override
    public void setDouble(String id, double value) {
        myMap.put(id, value);
    }

    @Override
    public List<Double> getDoubleList(String id) {
        return this.<Double>getList(id);
    }

    @Override
    public String getString(String id) {
        if(myMap.get(id) instanceof String val) return val;
        return "";
    }

    @Override
    public void setString(String id, String value) {
        myMap.put(id, value);
    }

    @Override
    public List<String> getStringList(String id) {
        return this.<String>getList(id);
    }

    @Override
    public @Nullable ItemStack getItemStack(String id) {
        if(myMap.get(id) instanceof ItemStack is) return is;
        return null;
    }

    @Override
    public void setItemStack(String id, ItemStack value) {
        myMap.put(id, value);
    }

    @Override
    public List<ItemStack> getItemStackList(String id) {
        return this.<ItemStack>getList(id);
    }

    @Override
    public DSerializationWrapper editMap(String id) {
        return new MapWrapper(serialization, (Map<String, Object>) myMap.get(id));
    }

    @Override
    public List<Map<String, ?>> editList(String id) {
        return (List<Map<String, ?>>)myMap.get(id);
    }

    @Override
    public void save(){
        serialization.saveConfig();
    }

    @Override
    public ISerialization getSerialization(){
        return serialization;
    }
}
