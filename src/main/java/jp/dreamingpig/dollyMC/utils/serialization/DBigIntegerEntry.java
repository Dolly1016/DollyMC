package jp.dreamingpig.dollyMC.utils.serialization;

import java.math.BigInteger;

class DBigIntegerEntry extends DPrimitiveAbstractEntry<BigInteger> {
    private BigInteger cache;
    public DBigIntegerEntry(DSerializationWrapper wrapper, String id, BigInteger defaultValue){
        super(wrapper, id);

        if(wrapper.contains(id))
            cache = new BigInteger(wrapper.getString(id));
        else
            cache = defaultValue;
    }

    @Override
    public BigInteger get(){
        return cache;
    }

    @Override
    public void set(BigInteger value){
        cache = value;
        wrapper.setString(id, value.toString());
    }
}
