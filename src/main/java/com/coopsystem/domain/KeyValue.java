package com.coopsystem.domain;

import java.io.Serializable;

/**
 * Created by Dariusz Ł on 02.05.2017.
 * This class is create for send map entry
 */
public class KeyValue<T, S> implements Serializable {
    private T key;
    private S value;

    public  KeyValue create(T key, S value) {
        KeyValue instance = new KeyValue<T, S>();
        instance.setKey(key);
        instance.setValue(value);
        return instance;
    }

    public void add(T key, S value) {
        this.setKey(key);
        this.setValue(value);
    }
    public S getValue() {
        return value;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public void setValue(S value) {
        this.value = value;
    }
}
