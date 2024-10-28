package jp.dreamingpig.dollyMC.utils.serialization;

public class DDoubleEntry extends DPrimitiveAbstractEntry<Double> {
    private double cache;
    public DDoubleEntry(DSerializationWrapper wrapper, String id, double defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = wrapper.getDouble(id);
        else
            cache = defaultValue;
    }

    @Override
    public Double get(){
        return cache;
    }

    @Override
    public void set(Double value){
        cache = value;
        wrapper.setDouble(id, value);
    }
}
