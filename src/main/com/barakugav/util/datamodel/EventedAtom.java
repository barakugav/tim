package com.barakugav.util.datamodel;

import java.util.Map;
import java.util.Objects;

class EventedAtom extends ViewAtomAbstract {

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
