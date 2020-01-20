package com.barakugav.util.datamodel;

import java.util.Map;

public interface Instance extends Atom {

    /**
     * Get a property, if the instance doesn't contains this property the property
     * will be get from the template
     */
    @Override
    public <V> V getProperty(String key);

    /**
     * return true if this instance or the template contains a property
     */
    @Override
    public boolean containsProperty(String key);

    /**
     * Get all non hidden properties of this instance and the template combined.
     * Properties from the template will be overwritten by the instance
     */
    @Override
    default Map<String, Object> getProperties() {
	return getProperties(false);
    }

    /**
     * Get all properties of this instance and the template combined. Properties
     * from the template will be overwritten by the instance
     */
    @Override
    public Map<String, Object> getProperties(boolean includeHidden);

    /**
     * Return if this instance is still alive. if the template was deleted this
     * instance will be deleted too
     */
    @Override
    public boolean isAlive();

    public Template getTemplate();

}
