package com.barakugav.util.datamodel;

import java.util.Map;

class InstanceImpl extends AtomImpl implements Instance {

    private static final String PROPERTY_TEMPLATE = ".Template";

    InstanceImpl(Model model, Template template) {
	super(model);
	setTemplate(template);
    }

    InstanceImpl(Model model, ID id, Map<String, Object> properties) {
	super(model, id, properties);
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
    public Template getTemplate() {
	return (Template) getModel().getAtom(template());
    }

    private ID template() {
	return getProperty(PROPERTY_TEMPLATE);
    }

    private boolean setTemplate(Template template) {
	return setProperty(PROPERTY_TEMPLATE, template.getID());
    }

}
