package com.barakugav.util.datamodel;

import java.util.Map;
import java.util.Objects;

public class POJOInstance extends POJOAtom implements Instance0 {

    private Template0 template;

    POJOInstance(ID id) {
	super(id);
    }

    @Override
    public <V> V getProperty(String key) {
	if (super.containsProperty(key))
	    return super.getProperty(key);
	return getTemplate().getProperty(key);
    }

    @Override
    public boolean containsProperty(String key) {
	return super.containsProperty(key) || getTemplate().containsProperty(key);
    }

    @Override
    public Map<String, Object> getProperties(boolean includeHidden) {
	Map<String, Object> result = getTemplate().getProperties(includeHidden);
	result.putAll(super.getProperties(includeHidden));
	return result;
    }

    @Override
    public Template0 getTemplate() {
	return template;
    }

    @Override
    public boolean setTemplate(Template0 template) {
	if (Objects.equals(template, this.template))
	    return false;
	this.template = template;
	return true;
    }

}
