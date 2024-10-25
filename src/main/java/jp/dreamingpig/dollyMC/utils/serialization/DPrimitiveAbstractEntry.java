package jp.dreamingpig.dollyMC.utils.serialization;

public abstract class DPrimitiveAbstractEntry<T> implements DPrimitiveEntry<T> {
    protected DSerializationWrapper wrapper;
    protected String id;

    public DPrimitiveAbstractEntry(DSerializationWrapper wrapper, String id){
        this.wrapper = wrapper;
        this.id = id;
    }

    @Override
    public abstract T get();
    @Override
    public abstract void set(T value);

    @Override
    public void save(){
        wrapper.save();
    }
}
