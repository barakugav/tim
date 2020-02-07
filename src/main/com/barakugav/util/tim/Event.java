package com.barakugav.util.tim;

import java.util.Objects;

public class Event {

    private final Object source;
    private final Object data;
    private final long timestamp;

    Event(Object source, Object data) {
	this.source = Objects.requireNonNull(source);
	this.data = data;
	timestamp = System.currentTimeMillis();
    }

    public Object getSource() {
	return source;
    }

    public Object getData() {
	return data;
    }

    public long getTimestamp() {
	return timestamp;
    }

}
