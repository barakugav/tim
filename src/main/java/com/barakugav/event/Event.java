package com.barakugav.event;

import java.util.Objects;

public final class Event {

    private final String topic;
    private final String key;
    private final byte[] data;
    private final long version;

    Event(String topic, String key, byte[] data, long version) {
	this.topic = Objects.requireNonNull(topic);
	this.key = key;
	this.data = data;
	this.version = version;
    }

    public String getTopic() {
	return topic;
    }

    public String getKey() {
	return key;
    }

    public Object getData(Deserializer deserializer) throws ClassNotFoundException {
	return deserializer.deserialize(data);
    }

    public long getVersion() {
	return version;
    }

}
