package jp.dreamingpig.dollyMC.utils.serialization;

class DIntegerEntry extends DPrimitiveAbstractEntry<Integer> {
    private int cache;
    public DIntegerEntry(DSerializationWrapper wrapper, String id, int defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = wrapper.getInt(id);
        else
            cache = defaultValue;
    }

    @Override
    public Integer get(){
        return cache;
    }

    @Override
    public void set(Integer value){
        cache = value;
        wrapper.setInt(id, value);
    }
}
