package com.barakugav.util.datamodel;

import java.util.Map;

interface ViewAtom extends Atom0 {

    @Override
    default ID getID() {
	return atom().getID();
    }

    @Override
    default <V> V getProperty(String key) {
	return atom().getProperty(key);
    }

    @Override
    default boolean containsProperty(String key) {
	return atom().containsProperty(key);
    }

    @Override
    default Map<String, Object> getProperties() {
	return atom().getProperties();
    }

    @Override
    default Map<String, Object> getProperties(boolean includeHidden) {
	return atom().getProperties(includeHidden);
    }

    @Override
    default <V> boolean setProperty(String key, V value) {
	return atom().setProperty(key, value);
    }

    @Override
    default boolean setProperties(Map<String, ? extends Object> properties) {
	return atom().setProperties(properties);
    }

    @Override
    default boolean delete() {
	return atom().delete();
    }

    @Override
    default boolean isAlive() {
	return atom().isAlive();
    }

    Atom0 atom();

}
