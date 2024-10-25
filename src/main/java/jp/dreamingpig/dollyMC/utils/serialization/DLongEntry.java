package jp.dreamingpig.dollyMC.utils.serialization;

class DLongEntry extends DPrimitiveAbstractEntry<Long> {
    private long cache;
    public DLongEntry(DSerializationWrapper wrapper, String id, long defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = wrapper.getLong(id);
        else
            cache = defaultValue;
    }

    @Override
    public Long get(){
        return cache;
    }

    @Override
    public void set(Long value){
        cache = value;
        wrapper.setLong(id, value);
    }
}