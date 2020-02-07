package com.barakugav.util.tim;

import java.util.Map;
import java.util.Objects;

class AtomLayerListeners implements AtomLayer {

    private final EventManager eventManager;

    AtomLayerListeners(EventManager eventManager) {
	this.eventManager = Objects.requireNonNull(eventManager);
    }

    @Override
    public Template0 layer(Template0 template) {
	return new EventedTemplate(template, eventManager);
    }

    @Override
    public Instance0 layer(Instance0 instance) {
	return new EventedInstance(instance, eventManager);
    }

    private static class EventedAtom extends ViewAtomAbstract {

	private final EventManager eventManager;

	EventedAtom(Atom0 atom, EventManager eventManager) {
	    super(atom);
	    this.eventManager = Objects.requireNonNull(eventManager);
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    if (!atom().setProperty(key, value))
		return false;
	    V newValue = getProperty(key);
	    eventManager().fireAtomPropertyChange(AtomEvent.newChangeEvent(getID(), key, newValue));
	    return true;
	}

	@Override
	public boolean setProperties(Map<String, ? extends Object> properties) {
	    if (!atom().setProperties(properties))
		return false;
	    Map<String, Object> newValue = getProperties(true);
	    eventManager().fireAtomPropertyChange(AtomEvent.newChangeEvent(getID(), "properties", newValue));
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    if (!atom().removeProperty(key))
		return false;
	    eventManager().fireAtomPropertyChange(AtomEvent.newChangeEvent(getID(), key, null));
	    return true;
	}

	@Override
	public boolean delete() {
	    if (isAlive())
		eventManager().fireAtomBeforeDelete(AtomEvent.newBeforeDeleteEvent(getID()));
	    if (!atom().delete())
		return false;
	    eventManager().fireAtomDeleted(AtomEvent.newDeleteEvent(getID()));
	    return true;
	}

	EventManager eventManager() {
	    return eventManager;
	}

    }

    private static class EventedTemplate extends EventedAtom implements ViewTemplate {

	EventedTemplate(Template0 template, EventManager eventManager) {
	    super(template, eventManager);
	}

	@Override
	public boolean delete() {
	    if (!atom().delete())
		return false;
	    eventManager().fireTemplateDeleted(new TableEvent(getID()));
	    return true;
	}

	@Override
	public Template0 atom() {

	    return (Template0) super.atom();
	}

    }

    private static class EventedInstance extends EventedAtom implements ViewInstance {

	EventedInstance(Instance0 instance, EventManager eventManager) {
	    super(instance, eventManager);
	}

	@Override
	public boolean delete() {
	    if (!atom().delete())
		return false;
	    eventManager().fireInstanceDeleted(new TableEvent(getID()));
	    return true;
	}

	@Override
	public Instance0 atom() {
	    return (Instance0) super.atom();
	}

    }

}
