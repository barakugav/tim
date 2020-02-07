package com.barakugav.util.tim;

public class AtomEvent extends Event {

    private final String property;

    @Deprecated
    AtomEvent(ID source, String property, Object data) {
	super(source, data);
	this.property = property;
    }

    @Override
    public ID getSource() {
	return (ID) super.getSource();
    }

    public String getPropertyKey() {
	return property;
    }

    static AtomEvent newDeleteEvent(ID id) {
	return new AtomEvent(id, null, null);
    }

    static AtomEvent newBeforeDeleteEvent(ID id) {
	return new AtomEvent(id, null, null);
    }

    static AtomEvent newChangeEvent(ID id, String property, Object data) {
	return new AtomEvent(id, property, data);
    }

}
