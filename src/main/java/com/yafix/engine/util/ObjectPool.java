package com.yafix.engine.util;

public class ObjectPool<T> {


    private final int initialSize;
    private final Class<T> clazz;

    public ObjectPool(int initialSize, Class<T> clazz) {
        this.initialSize = initialSize;
        this.clazz = clazz;
    }

    //dummy implementation todo: reimplement
    public T getObject() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
