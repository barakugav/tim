package com.barakugav.event;

public interface EventProducer {

    public String getTopic();

    public Serializer getSerializer();

    default void postEvent(String key, Object data) {
	postEvent(key, data, System.currentTimeMillis());
    }

    default void postEvent(String key, Object data, long version) {
	byte[] bytes = getSerializer().serialize(data);
	postEvent(new Event(getTopic(), key, bytes, version));
    }

    public void postEvent(Event event);

}
