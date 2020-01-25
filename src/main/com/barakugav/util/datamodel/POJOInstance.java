package com.barakugav.util.datamodel;

import java.util.Map;

public class POJOInstance extends POJOAtom implements Instance0 {

    private static final String PROPERTY_TEMPLATE = ".Template";

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
	return getProperty(PROPERTY_TEMPLATE);
    }

    @Override
    public void setTemplate(Template0 template) {
	setTemplate0(template);
    }

    private boolean setTemplate0(Template template) {
	return setProperty(PROPERTY_TEMPLATE, template);
    }

}
