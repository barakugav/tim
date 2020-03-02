package com.barakugav.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EventCenter {

    private final Map<String, Topic> topics;

    private static final EventCenter INSTANCE = new EventCenter();

    private EventCenter() {
	topics = new ConcurrentHashMap<>();
    }

    public static EventProducer newProducer(String topic) {
	return new EventProducerImpl(topic(topic));
    }

    public static EventCunsumer newCunsumer(String topic) {
	return new EventCunsumerImpl(topic(topic));
    }

    private static Topic topic(String name) {
	return INSTANCE.topics.computeIfAbsent(Objects.requireNonNull(name), Topic::new);
    }

    private static class Topic {

	private final String name;
	private final List<Event> events;

	Topic(String name) {
	    this.name = Objects.requireNonNull(name);
	    events = new ArrayList<>();
	}

    }

    private static class EventProducerImpl implements EventProducer {

	private final Topic topic;
	private Serializer serializer;

	EventProducerImpl(Topic topic) {
	    this.topic = Objects.requireNonNull(topic);
	    serializer = new DefaultSerializer();
	}

	@Override
	public String getTopic() {
	    return topic.name;
	}

	@Override
	public Serializer getSerializer() {
	    return serializer;
	}

	@Override
	public void postEvent(Event event) {
	    Objects.requireNonNull(event);
	    assert event.getTopic().equals(topic.name);

	    synchronized (topic.events) {
		topic.events.add(event);
	    }
	}

    }

    private static class EventCunsumerImpl implements EventCunsumer {

	private final Topic topic;
	private Deserializer deserializer;
	private int curser;

	EventCunsumerImpl(Topic topic) {
	    this.topic = Objects.requireNonNull(topic);
	    deserializer = new DefaultDeserializer();
	    curser = 0;
	}

	@Override
	public String getTopic() {
	    return topic.name;
	}

	@Override
	public Event nextEvent() {
	    synchronized (topic.events) {
		if (topic.events.size() <= curser)
		    return null;
		else
		    return topic.events.get(curser++);
	    }
	}

	@Override
	public Deserializer getDeserializer() {
	    return deserializer;
	}

    }

}
