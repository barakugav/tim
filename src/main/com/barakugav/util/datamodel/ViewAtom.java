package com.barakugav.util.datamodel;

import java.util.Map;

abstract class ViewAtom implements Atom0 {

    ViewAtom() {
    }

    @Override
    public ID getID() {
	return atom().getID();
    }

    @Override
    public <V> V getProperty(String key) {
	return atom().getProperty(key);
    }

    @Override
    public boolean containsProperty(String key) {
	return atom().containsProperty(key);
    }

    @Override
    public Map<String, Object> getProperties() {
	return atom().getProperties();
    }

    @Override
    public Map<String, Object> getProperties(boolean includeHidden) {
	return atom().getProperties(includeHidden);
    }

    @Override
    public <V> boolean setProperty(String key, V value) {
	return atom().setProperty(key, value);
    }

    @Override
    public boolean setProperties(Map<String, Object> properties) {
	return atom().setProperties(properties);
    }

    @Override
    public boolean delete() {
	return atom().delete();
    }

    @Override
    public boolean isAlive() {
	return atom().isAlive();
    }

    @Override
    public String toString() {
	return atom().toString();
    }

    @Override
    public boolean equals(Object other) {
	return other == this || atom().equals(other);
    }

    @Override
    public int hashCode() {
	return atom().hashCode();
    }

    abstract Atom atom();

}
