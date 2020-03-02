package com.barakugav.tim;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import com.barakugav.event.EventProducer;

class AtomLayerListeners implements AtomLayer {

    private final EventProducer eventProducer;

    AtomLayerListeners(EventProducer eventProducer) {
	this.eventProducer = Objects.requireNonNull(eventProducer);
    }

    @Override
    public Template0 layer(Template0 template) {
	return new EventedTemplate(template, eventProducer);
    }

    @Override
    public Instance0 layer(Instance0 instance) {
	return new EventedInstance(instance, eventProducer);
    }

    private static abstract class EventedAtom extends AbstractViewAtom {

	private final EventProducer eventProducer;

	EventedAtom(Atom0 atom, EventProducer eventProducer) {
	    super(atom);
	    this.eventProducer = Objects.requireNonNull(eventProducer);
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    DTOAtom before = toDTO();
	    if (!atom().setProperty(key, value))
		return false;
	    postEvent(before);
	    return true;
	}

	@Override
	public boolean setProperties(Map<String, ? extends Object> properties) {
	    DTOAtom before = toDTO();
	    if (!atom().setProperties(properties))
		return false;
	    postEvent(before);
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    DTOAtom before = toDTO();
	    if (!atom().removeProperty(key))
		return false;
	    postEvent(before);
	    return true;
	}

	@Override
	public boolean delete() {
	    DTOAtom before = toDTO();
	    if (!atom().delete())
		return false;
	    postEvent(before);
	    return true;
	}

	EventProducer eventProducer() {
	    return eventProducer;
	}

	DTOAtom toDTO() {
	    return DTOAtom.valueOf(this);
	}

	void postEvent(DTOAtom before) {
	    AtomChangeLog changeLog = new AtomChangeLog(before, toDTO());
	    eventProducer().postEvent(getID().toString(), changeLog, getVersion());
	}

    }

    private static class EventedTemplate extends EventedAtom implements ViewTemplate {

	EventedTemplate(Template0 template, EventProducer eventProducer) {
	    super(template, eventProducer);
	}

	@Override
	public Template0 atom() {
	    return (Template0) super.atom();
	}

	@Override
	public boolean addInstance(ID instance) {
	    DTOAtom before = toDTO();
	    if (!atom().addInstance(instance))
		return false;
	    postEvent(before);
	    return true;
	}

	@Override
	public boolean removeInstance(ID instance) {
	    DTOAtom before = toDTO();
	    if (!atom().removeInstance(instance))
		return false;
	    postEvent(before);
	    return true;
	}

	@Override
	public boolean setInstances(Collection<ID> instances) {
	    DTOAtom before = toDTO();
	    if (!atom().setInstances(instances))
		return false;
	    postEvent(before);
	    return true;
	}

    }

    private static class EventedInstance extends EventedAtom implements ViewInstance {

	EventedInstance(Instance0 instance, EventProducer eventProducer) {
	    super(instance, eventProducer);
	}

	@Override
	public Instance0 atom() {
	    return (Instance0) super.atom();
	}

	@Override
	public boolean setTemplate(ID template) {
	    DTOAtom before = toDTO();
	    if (!atom().setTemplate(template))
		return false;
	    postEvent(before);
	    return true;
	}

    }

}
