package com.barakugav.tim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

class AtomTester {

    static void getId(Atom atom, ID expected) {
	assertEquals(expected, atom.getID());
    }

    static void getTemplate(Instance instance, Template expected) {
	assertEquals(expected, instance.getTemplate());
    }

    static void setTemplate(Instance0 instance, Template template) {
	assertTrue(instance.setTemplate(template.getID()));
	assertEquals(template, instance.getTemplate());
    }

    static void isAlive(Atom atom) {
	assertTrue(atom.isAlive());
    }

    static void delete(Atom atom) {
	assertTrue(atom.delete());
    }

    static void deleteAndIsAliveFalse(Atom atom) {
	atom.delete();
	assertFalse(atom.isAlive());
    }

    static void deleteTwise(Atom atom) {
	atom.delete();
	assertFalse(atom.delete());
    }

    static void deleteTemplate(Instance instance) {
	Template template = instance.getTemplate();
	template.delete();
	assertFalse(instance.isAlive());
    }

    static void getPropertyNoProperty(Atom atom) {
	assertNull(atom.getProperty("key"));
    }

    static void getPropertyNullKey(Atom atom) {
	assertNull(atom.getProperty(null));
    }

    static void getPropertiesEmpty(Atom atom) {
	Map<String, Object> p = atom.getProperties();
	assertTrue(p.isEmpty());
    }

    static void getPropertiesNonEmpty(Atom atom) {
	String key1 = "key1";
	String key2 = "key2";
	String key3 = "key3";
	Object value1 = "value1";
	Object value2 = "value2";
	Object value3 = "value3";
	Object value4 = "value4";
	atom.setProperty(key1, value1);
	atom.setProperty(key2, value2);
	atom.setProperty(key3, value3);
	atom.setProperty(key2, value4);
	Map<String, Object> expected = Map.of(key1, value1, key2, value4, key3, value3);
	Map<String, Object> actual = atom.getProperties();
	assertEquals(expected, actual);
    }

    static void setProperty(Atom atom) {
	assertTrue(atom.setProperty("key", "value"));
    }

    static void setPropertyAndGetString(Atom atom) {
	String key = "key";
	Object value = "value";
	atom.setProperty(key, value);
	assertEquals(value, atom.getProperty(key));
    }

    static void setPropertyAndGetNumber(Atom atom) {
	String key = "key";
	Object value = Long.valueOf(578L);
	atom.setProperty(key, value);
	assertEquals(value, atom.getProperty(key));
    }

    static void setPropertyAndGetSet(Atom atom) {
	String key = "key";
	@SuppressWarnings("boxing")
	Set<Integer> value = Set.of(5, 3);
	atom.setProperty(key, value);
	assertEquals(value, atom.getProperty(key));
    }

    static void containsPropertyFalse(Atom atom) {
	assertFalse(atom.containsProperty("key"));
    }

    static void containsPropertyTrue(Atom atom) {
	String key = "key";
	atom.setProperty(key, new Object());
	assertTrue(atom.containsProperty(key));
    }

    static void removePropertyTrue(Atom atom) {
	String key = "key";
	atom.setProperty(key, new Object());
	assertTrue(atom.removeProperty(key));
    }

    static void removePropertyFalse(Atom atom) {
	String key = "key";
	assertFalse(atom.removeProperty(key));
    }

    static void removePropertyAndGet(Atom atom) {
	String key = "key";
	atom.setProperty(key, new Object());
	atom.removeProperty(key);
	assertNull(atom.getProperty(key));
    }

    static void removePropertyAndContains(Atom atom) {
	String key = "key";
	atom.setProperty(key, new Object());
	atom.removeProperty(key);
	assertFalse(atom.containsProperty(key));
    }

    static void getInstancesZero(Template template) {
	assertEquals(Collections.emptyList(), new ArrayList<>(template.getInstances()));
    }

    static void getInstancesOne(Template template, Instance expected) {
	assertEquals(List.of(expected), new ArrayList<>(template.getInstances()));
    }

    static void getInstancesCountZero(Template template) {
	assertEquals(0, template.getInstanceCount());
    }

    static void getInstancesCountOne(Instance instance) {
	Template template = instance.getTemplate();
	assertEquals(1, template.getInstanceCount());
    }

}
