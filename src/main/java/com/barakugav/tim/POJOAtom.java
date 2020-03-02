package com.barakugav.tim;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class POJOAtom extends AbstractAtom implements Atom0 {

    private final ID id;
    private final Map<String, Object> properties;
    private boolean isAlive;
    long version;

    final AtomResolver resolver;

    POJOAtom(ID id, AtomResolver resolver) {
	this.id = Objects.requireNonNull(id);
	this.properties = new HashMap<>();
	isAlive = true;

	this.resolver = Objects.requireNonNull(resolver);
    }

    @Override
    public ID getID() {
	return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getProperty(String key) {
	return (V) properties.get(key);
    }

    @Override
    public boolean containsProperty(String key) {
	return properties.containsKey(key);
    }

    @Override
    public Map<String, Object> getProperties() {
	return new HashMap<>(properties);
    }

    @Override
    public <V> boolean setProperty(String key, V value) {
	if (!Atom.isValidPropertyKey(key) || Objects.equals(value, properties.put(key, value)))
	    return false;
	version++;
	return true;
    }

    @Override
    public boolean setProperties(Map<String, ? extends Object> properties) {
	if (properties == null)
	    properties = Collections.emptyMap();
	Map<String, Object> newProperties = new HashMap<>(properties);
	newProperties.keySet().removeIf(k -> !Atom.isValidPropertyKey(k));

	if (Objects.equals(newProperties, this.properties))
	    return false;
	this.properties.clear();
	this.properties.putAll(newProperties);
	version++;
	return true;
    }

    @Override
    public boolean removeProperty(String key) {
	if (!properties.containsKey(key))
	    return false;
	properties.remove(key);
	version++;
	return true;
    }

    @Override
    public boolean delete() {
	if (!isAlive)
	    return false;
	isAlive = false;
	version++;
	return true;
    }

    @Override
    public long getVersion() {
	return version;
    }

    @Override
    public boolean isAlive() {
	return isAlive;
    }

    @Override
    public String toString() {
	return "Atom[" + id + "]";
    }

}
