package jp.dreamingpig.dollyMC.utils.serialization;

public interface DPrimitiveEntry<T> {
    T get();
    void set(T value);

    void save();
}
