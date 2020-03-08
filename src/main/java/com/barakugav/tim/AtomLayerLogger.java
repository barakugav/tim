package com.barakugav.tim;

import java.util.Map;
import java.util.Objects;

import com.barakugav.tim.log.ModelLog;
import com.barakugav.tim.log.ModelLogger;

class AtomLayerLogger implements AtomLayer {

    private final ModelLogger logger;
    private volatile boolean enabled;

    AtomLayerLogger(ModelLogger logger) {
	this.logger = Objects.requireNonNull(logger);
	enabled = false;
    }

    void disable() {
	enabled = false;
    }

    void enable() {
	enabled = true;
    }

    @Override
    public Template0 layer(Template0 template) {
	return new LoggerTemplate(template);
    }

    @Override
    public Instance0 layer(Instance0 instance) {
	return new LoggerInstance(instance);
    }

    private class LoggerAtom extends AbstractViewAtom {

	LoggerAtom(Atom0 atom) {
	    super(atom);
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    V oldValue = getProperty(key);
	    if (!atom().setProperty(key, value))
		return false;
	    V newValue = getProperty(key);
	    log(ModelLog.newChangeLog(getID(), key, oldValue, newValue));
	    return true;
	}

	@Override
	public boolean setProperties(Map<String, ? extends Object> properties) {
	    Map<String, Object> oldValue = getProperties();
	    if (!atom().setProperties(properties))
		return false;
	    Map<String, Object> newValue = getProperties();
	    log(ModelLog.newChangeLog(getID(), "properties", oldValue, newValue));
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    Object oldValue = getProperty(key);
	    if (!atom().removeProperty(key))
		return false;
	    log(ModelLog.newChangeLog(getID(), key, oldValue, null));
	    return true;
	}

	@Override
	public boolean delete() {
	    if (!atom().delete())
		return false;
	    log(ModelLog.newDeleteLog(getID()));
	    return true;
	}

	void log(ModelLog log) {
	    if (enabled)
		logger.log(log);
	}

    }

    private class LoggerTemplate extends LoggerAtom implements ViewTemplate {

	LoggerTemplate(Template0 template) {
	    super(template);
	}

	@Override
	public Template0 atom() {
	    return (Template0) super.atom();
	}

    }

    private class LoggerInstance extends LoggerAtom implements ViewInstance {

	LoggerInstance(Instance0 instance) {
	    super(instance);
	}

	@Override
	public boolean setTemplate(ID template) {
	    ID oldValue = getTemplate0();
	    if (!atom().setTemplate(template))
		return false;
	    ID newValue = getTemplate0();
	    log(ModelLog.newChangeLog(getID(), "template", oldValue, newValue));
	    return true;
	}

	@Override
	public Instance0 atom() {
	    return (Instance0) super.atom();
	}

    }

}
