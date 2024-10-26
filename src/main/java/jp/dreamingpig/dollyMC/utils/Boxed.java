package jp.dreamingpig.dollyMC.utils;

/**
 * 値をボックス化します。
 * ラムダ式内でローカル変数の値を書き換える際に使用できます。
 * @param <T>データの型
 */
public class Boxed<T> {
    private T value;
    public Boxed(T value){
        this.value = value;
    }

    public T get(){
        return value;
    }
    public void set(T value){
        this.value = value;
    }
}
