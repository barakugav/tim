package com.barakugav.util.datamodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

abstract class AtomImpl implements Atom {

    private final Model model;
    private final ID id;
    private final Map<String, Object> properties;

    AtomImpl(Model model) {
	this(model, null, null);
    }

    AtomImpl(Model model, ID id, Map<String, Object> properties) {
	this.model = Objects.requireNonNull(model);
	this.id = id != null ? id : ID.newID();
	this.properties = properties != null ? new HashMap<>(properties) : new HashMap<>();

	this.model.addAtom(this);
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
	return model.removeAtom(this);
    }

    @Override
    public boolean isAlive() {
	return model.getAtom(getID()) == this;
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

    Model getModel() {
	return model;
    }

}
