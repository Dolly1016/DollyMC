package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DItemStackEntry extends DPrimitiveAbstractEntry<ItemStack> {
    @Nullable
    private ItemStack cache;
    public DItemStackEntry(DSerializationWrapper wrapper, String id, @Nullable ItemStack defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = wrapper.getItemStack(id);
        else
            cache = defaultValue;
    }

    @Override
    @Nullable
    public ItemStack get(){
        return cache;
    }

    @Override
    public void set(@Nullable ItemStack value){
        cache = value;
        wrapper.setItemStack(id, value);
    }
}