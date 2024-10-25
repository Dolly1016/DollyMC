package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class DStructureEntry {
    private DSerializationWrapper wrapper;

    public DStructureEntry(DSerializationWrapper wrapper){
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

    public List<Double> getDoubleList(String id){
        return wrapper.getDoubleList(id);
    }

    public List<String> getStringList(String id){
        return wrapper.getStringList(id);
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

    public void save(){
        wrapper.save();
    }
}
