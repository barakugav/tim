package com.barakugav.tim;

import java.util.Map;
import java.util.Objects;

class POJOInstance extends POJOAtom implements Instance0 {

    private ID template;

    POJOInstance(ID id, AtomResolver resolver) {
	super(id, resolver);
    }

    @Override
    public <V> V getProperty(String key) {
	return super.containsProperty(key) ? super.getProperty(key) : getTemplate().getProperty(key);
    }

    @Override
    public boolean containsProperty(String key) {
	return super.containsProperty(key) || getTemplate().containsProperty(key);
    }

    @Override
    public Map<String, Object> getProperties() {
	Map<String, Object> result = getTemplate().getProperties();
	result.putAll(super.getProperties());
	return result;
    }

    @Override
    public Template0 getTemplate() {
	return resolver.getTemplate(template);
    }

    @Override
    public ID getTemplate0() {
	return template;
    }

    @Override
    public boolean setTemplate(ID template) {
	if (Objects.equals(template, this.template))
	    return false;
	this.template = template;
	version++;
	return true;
    }

    @Override
    public boolean delete() {
	if (!super.delete())
	    return false;
	getTemplate().removeInstance(getID());
	return true;
    }

}
