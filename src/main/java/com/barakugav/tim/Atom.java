package com.barakugav.tim;

import java.util.Map;

public interface Atom {

    public ID getID();

    public <V> V getProperty(String key);

    public boolean containsProperty(String key);

    public Map<String, Object> getProperties();

    public <V> boolean setProperty(String key, V value);

    public boolean setProperties(Map<String, ? extends Object> properties);

    public boolean removeProperty(String key);

    public long getVersion();
    
    public boolean delete();

    public boolean isAlive();

    public static boolean isValidPropertyKey(String key) {
	return key != null && !key.isBlank();// && key.matches("[a-zA-Z][a-zA-Z0-9_]*");
    }

}
