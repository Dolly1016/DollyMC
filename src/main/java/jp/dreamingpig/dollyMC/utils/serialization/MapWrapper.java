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

    public MapWrapper(ISerialization serialization, LinkedHashMap<String, Object> map){
        this.serialization = serialization;
        this.myMap = map;
    }

    @Override
    public boolean contains(String id){
        return myMap.containsKey(id);
    }

    @Override
    public int getInt(String id){
        return (Integer)myMap.get(id);
    }
    @Override
    public void setInt(String id, int value){
        myMap.put(id, value);
    }
    @Override
    public List<Integer> getIntList(String id){
        if(myMap.containsKey(id)){
            var list = myMap.get(id);
            return (List<Integer>)list;
        }else{
            List<Integer> list = new ArrayList<>();
            myMap.put(id, list);
            return list;
        }
    }

    @Override
    public long getLong(String id) {
        return 0;
    }

    @Override
    public void setLong(String id, long value) {

    }

    @Override
    public List<Long> getLongList(String id) {
        return List.of();
    }

    @Override
    public double getDouble(String id) {
        return (Double)myMap.get(id);
    }

    @Override
    public void setDouble(String id, double value) {
        myMap.put(id, value);
    }

    @Override
    public List<Double> getDoubleList(String id) {
        return List.of();
    }

    @Override
    public String getString(String id) {
        return (String)myMap.get(id);
    }

    @Override
    public void setString(String id, String value) {
        myMap.put(id, value);
    }

    @Override
    public List<String> getStringList(String id) {
        return List.of();
    }

    @Override
    public @Nullable ItemStack getItemStack(String id) {
        return null;
    }

    @Override
    public void setItemStack(String id, ItemStack value) {

    }

    @Override
    public List<ItemStack> getItemStackList(String id) {
        return List.of();
    }

    @Override
    public DSerializationWrapper editMap(String id) {
        return null;
    }

    @Override
    public List<LinkedHashMap<String, ?>> editList(String id) {
        return List.of();
    }

    /*
    @Override
    public long getLong(String id){
        return mySection.getLong(id);
    }
    @Override
    public void setLong(String id, long value){
        mySection.set(id, value);
    }
    @Override
    public List<Long> getLongList(String id){
        var list = mySection.getLongList(id);
        mySection.set(id, list);
        return list;
    }

    @Override
    public double getDouble(String id){
        return mySection.getDouble(id);
    }
    @Override
    public void setDouble(String id, double value){
        mySection.set(id, value);
    }
    @Override
    public List<Double> getDoubleList(String id){
        var list = mySection.getDoubleList(id);
        mySection.set(id, list);
        return list;
    }

    @Override
    public String getString(String id){
        return mySection.getString(id);
    }
    @Override
    public void setString(String id, String value){
        mySection.set(id, value);
    }
    @Override
    public List<String> getStringList(String id){
        var list = mySection.getStringList(id);
        mySection.set(id, list);
        return list;
    }

    @Override
    @Nullable
    public ItemStack getItemStack(String id){
        return mySection.getItemStack(id);
    }
    @Override
    public void setItemStack(String id, ItemStack value){
        mySection.set(id, value);
    }
    @Override
    public List<ItemStack> getItemStackList(String id){
        var rawList = mySection.getList(id, new ArrayList<ItemStack>());
        mySection.set(id, rawList);
        return (List<ItemStack>)rawList;
    }

    @Override
    public DSerializationWrapper editMap(String id){
        var section = mySection.getConfigurationSection(id);
        if(section == null) {
            section = new MemoryConfiguration();
        }
        mySection.set(id, section);
        return new SectionWrapper(serialization, section);
    }

    @Override
    public List<LinkedHashMap<String, ?>> editList(String id){
        var rawList = mySection.getList(id, new ArrayList<MemoryConfiguration>());
        mySection.set(id, rawList);
        return (List<LinkedHashMap<String, ?>>)rawList;
    }
    */

    @Override
    public void save(){
        serialization.saveConfig();
    }

    @Override
    public ISerialization getSerialization(){
        return serialization;
    }
}
