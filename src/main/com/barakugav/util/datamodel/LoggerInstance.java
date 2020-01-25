package com.barakugav.util.datamodel;

class LoggerInstance extends LoggerAtom implements ViewInstance {

    LoggerInstance(Instance0 instance, ModelLogger logger) {
	super(instance, logger);
    }

    @Override
    public boolean setTemplate(Template0 template) {
	Template0 oldValue = getTemplate();
	if (!atom().setTemplate(template))
	    return false;
	Template0 newValue = getTemplate();
	logger().log(ModelLog.newChangeLog(getID(), "template", oldValue, newValue));
	return true;
    }

    @Override
    public Instance0 atom() {
	return (Instance0) super.atom();
    }

}
