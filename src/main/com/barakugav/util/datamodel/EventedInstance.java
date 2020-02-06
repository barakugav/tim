package com.barakugav.util.datamodel;

class EventedInstance extends EventedAtom implements ViewInstance {

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
