package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class SectionWrapper implements DSerializationWrapper{
    private ISerialization serialization;
    private ConfigurationSection mySection;

    public SectionWrapper(ISerialization serialization){
        this.serialization = serialization;
        this.mySection = serialization.getConfig();
    }

    public SectionWrapper(ISerialization serialization, ConfigurationSection section){
        this.serialization = serialization;
        this.mySection = section;
    }

    @Override
    public boolean contains(String id){
        return mySection.contains(id);
    }

    @Override
    public int getInt(String id){
        return mySection.getInt(id);
    }
    @Override
    public void setInt(String id, int value){
        mySection.set(id, value);
    }
    @Override
    public List<Integer> getIntList(String id){
        var list = mySection.getIntegerList(id);
        mySection.set(id, list);
        return list;
    }

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
    public List<MemoryConfiguration> editList(String id){
        var rawList = mySection.getList(id, new ArrayList<MemoryConfiguration>());
        mySection.set(id, rawList);
        return (List<MemoryConfiguration>)rawList;
    }

    @Override
    public void save(){
        serialization.saveConfig();
    }

    @Override
    public ISerialization getSerialization(){
        return serialization;
    }

    @Override
    public ConfigurationSection getSection() {
        return mySection;
    }
}
