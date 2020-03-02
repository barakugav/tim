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

    public static EventCenter getInstance() {
	return INSTANCE;
    }

    public EventProducer newProducer(String topic) {
	return topic(topic).newProducer();
    }

    public EventCunsumer newCunsumer(String topic) {
	return topic(topic).newCunsumer();
    }

    private Topic topic(String topic) {
	return topics.computeIfAbsent(Objects.requireNonNull(topic), t -> new Topic(t));
    }

    private static class Topic {

	private final String name;
	private final List<Event> events;

	Topic(String topic) {
	    this.name = Objects.requireNonNull(topic);
	    events = new ArrayList<>();
	}

	EventProducer newProducer() {
	    return new EventProducerImpl(this);
	}

	EventCunsumer newCunsumer() {
	    return new EventCunsumerImpl(this);
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
