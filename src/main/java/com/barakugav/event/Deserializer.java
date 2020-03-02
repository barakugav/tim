package com.barakugav.event;

public interface Deserializer {

    public Object deserialize(byte[] bytes) throws ClassNotFoundException;

}
