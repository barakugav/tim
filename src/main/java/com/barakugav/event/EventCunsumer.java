package com.barakugav.event;

public interface EventCunsumer {

    public String getTopic();

    public Deserializer getDeserializer();

    public Event nextEvent();

}
