package com.barakugav.tim;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import com.barakugav.emagnetar.Producer;
import com.barakugav.tim.dto.DTOAtom;

class AtomLayerListeners implements AtomLayer {

    private final Producer eventProducer;

    AtomLayerListeners(Producer eventProducer) {
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

	private final Producer eventProducer;

	EventedAtom(Atom0 atom, Producer eventProducer) {
	    super(atom);
	    this.eventProducer = Objects.requireNonNull(eventProducer);
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    if (!atom().setProperty(key, value))
		return false;
	    postEvent();
	    return true;
	}

	@Override
	public boolean setProperties(Map<String, ? extends Object> properties) {
	    if (!atom().setProperties(properties))
		return false;
	    postEvent();
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    if (!atom().removeProperty(key))
		return false;
	    postEvent();
	    return true;
	}

	@Override
	public boolean delete() {
	    if (!atom().delete())
		return false;
	    postEvent();
	    return true;
	}

	void postEvent() {
	    String key = getID().toString();
	    Object data = DTOAtom.valueOf(this);
	    long version = getVersion();
	    eventProducer.postEvent(key, data, version);
	}

    }

    private static class EventedTemplate extends EventedAtom implements ViewTemplate {

	EventedTemplate(Template0 template, Producer eventProducer) {
	    super(template, eventProducer);
	}

	@Override
	public Template0 atom() {
	    return (Template0) super.atom();
	}

	@Override
	public boolean addInstance(ID instance) {
	    if (!atom().addInstance(instance))
		return false;
	    postEvent();
	    return true;
	}

	@Override
	public boolean removeInstance(ID instance) {
	    if (!atom().removeInstance(instance))
		return false;
	    postEvent();
	    return true;
	}

	@Override
	public boolean setInstances(Collection<ID> instances) {
	    if (!atom().setInstances(instances))
		return false;
	    postEvent();
	    return true;
	}

    }

    private static class EventedInstance extends EventedAtom implements ViewInstance {

	EventedInstance(Instance0 instance, Producer eventProducer) {
	    super(instance, eventProducer);
	}

	@Override
	public Instance0 atom() {
	    return (Instance0) super.atom();
	}

	@Override
	public boolean setTemplate(ID template) {
	    if (!atom().setTemplate(template))
		return false;
	    postEvent();
	    return true;
	}

    }

}
