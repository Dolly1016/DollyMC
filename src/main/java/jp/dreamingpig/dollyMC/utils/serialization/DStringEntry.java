package jp.dreamingpig.dollyMC.utils.serialization;

public class DStringEntry extends DPrimitiveAbstractEntry<String> {
    private String cache;
    public DStringEntry(DSerializationWrapper wrapper, String id, String defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = wrapper.getString(id);
        else
            cache = defaultValue;
    }

    @Override
    public String get(){
        return cache;
    }

    @Override
    public void set(String value){
        cache = value;
        wrapper.setString(id, value);
    }
}
