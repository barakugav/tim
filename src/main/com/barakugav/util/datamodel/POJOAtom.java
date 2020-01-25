package com.barakugav.util.datamodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class POJOAtom implements Atom0 {

    private final ID id;
    private final Map<String, Object> properties;
    private boolean isAlive;

    POJOAtom(ID id) {
	this.id = Objects.requireNonNull(id);
	this.properties = new HashMap<>();
	isAlive = true;
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
    public Map<String, Object> getProperties(boolean includeHidden) {
	Map<String, Object> result = new HashMap<>();
	if (!includeHidden)
	    result.keySet().removeIf(Atom::isHiddenProperty);
	return result;
    }

    @Override
    public <V> boolean setProperty(String key, V value) {
	return Atom.isValidPropertyKey(key) && !Objects.equals(value, properties.put(key, value));
    }

    @Override
    public boolean setProperties(Map<String, Object> properties) {
	if (properties == null)
	    properties = Collections.emptyMap();
	Map<String, Object> newProperties = new HashMap<>(properties);
	newProperties.keySet().removeIf(k -> !Atom.isValidPropertyKey(k));

	if (Objects.equals(newProperties, this.properties))
	    return false;
	this.properties.clear();
	this.properties.putAll(newProperties);
	return true;
    }

    @Override
    public boolean delete() {
	if (!isAlive)
	    return false;
	isAlive = false;
	return true;
    }

    @Override
    public boolean isAlive() {
	return isAlive;
    }

    @Override
    public String toString() {
	return "Atom[" + id + "]";
    }

    @Override
    public boolean equals(Object other) {
	if (other == this)
	    return true;
	if (!(other instanceof Atom))
	    return false;

	Atom o = (Atom) other;
	return o.getID().equals(id);
    }

    @Override
    public int hashCode() {
	return id.hashCode();
    }

}
