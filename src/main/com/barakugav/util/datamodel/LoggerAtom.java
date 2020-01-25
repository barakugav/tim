package com.barakugav.util.datamodel;

import java.util.Map;
import java.util.Objects;

class LoggerAtom extends ViewAtomAbstract {

    private final ModelLogger logger;

    LoggerAtom(Atom0 atom, ModelLogger logger) {
	super(atom);
	this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public <V> boolean setProperty(String key, V value) {
	V oldValue = getProperty(key);
	if (!atom().setProperty(key, oldValue))
	    return false;
	V newValue = getProperty(key);
	logger().log(ModelLog.newChangeLog(getID(), key, oldValue, newValue));
	return true;
    }

    @Override
    public boolean setProperties(Map<String, Object> properties) {
	Map<String, Object> oldValue = getProperties(true);
	if (!atom().setProperties(properties))
	    return false;
	Map<String, Object> newValue = getProperties(true);
	logger().log(ModelLog.newChangeLog(getID(), "properties", oldValue, newValue));
	return true;
    }

    @Override
    public boolean delete() {
	if (!atom().delete())
	    return false;
	logger().log(ModelLog.newDeleteLog(getID()));
	return true;
    }

    ModelLogger logger() {
	return logger;
    }

}
