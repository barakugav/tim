package com.barakugav.util.datamodel;

class EventedTemplate extends EventedAtom implements ViewTemplate {

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
