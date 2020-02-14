package com.barakugav.tim;

import java.util.Map;
import java.util.Objects;

class AtomLayerLogger implements AtomLayer {

    private final ModelLogger logger;

    AtomLayerLogger(ModelLogger logger) {
	this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public Template0 layer(Template0 template) {
	return new LoggerTemplate(template, logger);
    }

    @Override
    public Instance0 layer(Instance0 instance) {
	return new LoggerInstance(instance, logger);
    }

    private static class LoggerAtom extends AbstractViewAtom {

	private final ModelLogger logger;

	LoggerAtom(Atom0 atom, ModelLogger logger) {
	    super(atom);
	    this.logger = Objects.requireNonNull(logger);
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    V oldValue = getProperty(key);
	    if (!atom().setProperty(key, value))
		return false;
	    V newValue = getProperty(key);
	    logger().log(ModelLog.newChangeLog(getID(), key, oldValue, newValue));
	    return true;
	}

	@Override
	public boolean setProperties(Map<String, ? extends Object> properties) {
	    Map<String, Object> oldValue = getProperties(true);
	    if (!atom().setProperties(properties))
		return false;
	    Map<String, Object> newValue = getProperties(true);
	    logger().log(ModelLog.newChangeLog(getID(), "properties", oldValue, newValue));
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    Object oldValue = getProperty(key);
	    if (!atom().removeProperty(key))
		return false;
	    logger().log(ModelLog.newChangeLog(getID(), key, oldValue, null));
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

    private static class LoggerTemplate extends LoggerAtom implements ViewTemplate {

	LoggerTemplate(Template0 template, ModelLogger logger) {
	    super(template, logger);
	}

	@Override
	public Template0 atom() {
	    return (Template0) super.atom();
	}

    }

    private static class LoggerInstance extends LoggerAtom implements ViewInstance {

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

}
