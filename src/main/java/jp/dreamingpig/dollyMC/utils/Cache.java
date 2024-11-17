package jp.dreamingpig.dollyMC.utils;

import java.util.function.Supplier;

public class Cache<T> {
    private T cache = null;
    final private Supplier<T> supplier;

    public Cache(Supplier<T> supplier){
        this.cache = null;
        this.supplier = supplier;
    }
    public T get(){
        if(cache == null) cache = supplier.get();
        return cache;
    }
}
