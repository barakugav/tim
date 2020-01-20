package com.barakugav.util.datamodel;

import java.util.Map;

public interface Atom {

    public ID getID();

    public <V> V getProperty(String key);

    public boolean containsProperty(String key);

    default Map<String, Object> getProperties() {
	return getProperties(false);
    }

    public Map<String, Object> getProperties(boolean includeHidden);

    public <V> boolean setProperty(String key, V value);

    public boolean setProperties(Map<String, Object> properties);

    public boolean delete();

    public boolean isAlive();

    public static boolean isValidPropertyKey(String key) {
	return key != null && !key.isBlank();
    }

    public static boolean isHiddenProperty(String key) {
	return key != null && key.startsWith(".");
    }

}
