package jp.dreamingpig.dollyMC.utils.serialization;

import java.math.BigInteger;
import java.util.UUID;

class DUUIDEntry extends DPrimitiveAbstractEntry<UUID> {
    private UUID cache;
    public DUUIDEntry(DSerializationWrapper wrapper, String id, UUID defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = UUID.fromString(wrapper.getString(id));
        else
            cache = defaultValue;
    }

    @Override
    public UUID get(){
        return cache;
    }

    @Override
    public void set(UUID value){
        cache = value;
        wrapper.setString(id, value.toString());
    }
}
