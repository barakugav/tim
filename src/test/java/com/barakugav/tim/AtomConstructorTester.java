package com.barakugav.tim;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.Consumer;

class AtomConstructorTester {

    static void createTemplate(AtomConstructor c) {
	assertNotNull(c.newTemplate(ID.newID("table1")));
    }

    static void createInstance(AtomConstructor c) {
	assertNotNull(c.newInstance(ID.newID("table1")));
    }

    static void getId(AtomConstructor c) {
	ID templateID = ID.newID("table1");
	ID instanceID = ID.newID("table1");
	Template template = c.newTemplate(templateID);
	Instance instance = c.newInstance(instanceID);
	AtomTester.getId(template, templateID);
	AtomTester.getId(instance, instanceID);
    }

    static void getTemplate(AtomConstructor c) {
	ID instanceID = ID.newID("table1");
	Instance instance = c.newInstance(instanceID);
	AtomTester.getTemplate(instance, null);
    }

    static void setTemplate(AtomConstructor c) {
	ID templateID = ID.newID("table1");
	ID instanceID = ID.newID("table1");
	Template0 template = c.newTemplate(templateID);
	Instance0 instance = c.newInstance(instanceID);
	AtomTester.setTemplate(instance, template);
    }

    static void isAlive(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::isAlive);
    }

    static void delete(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::delete);
    }

    static void deleteAndIsAliveFalse(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::deleteAndIsAliveFalse);
    }

    static void deleteTwise(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::deleteTwise);

    }

    static void deleteTemplate(AtomConstructor c) {
	Instance instance = newInstance(c);
	AtomTester.deleteTemplate(instance);
    }

    static void getPropertyEmpty(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::getPropertiesEmpty);
    }

    static void getPropertyNullKey(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::getPropertyNullKey);
    }

    static void getPropertiesEmpty(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::getPropertiesEmpty);
    }

    static void getPropertiesNonEmpty(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::getPropertiesNonEmpty);
    }

    static void setProperty(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::setProperty);
    }

    static void setPropertyAndGetString(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::setPropertyAndGetString);
    }

    static void setPropertyAndGetNumber(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::setPropertyAndGetNumber);
    }

    static void setPropertyAndGetSet(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::setPropertyAndGetSet);
    }

    static void containsPropertyFalse(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::containsPropertyFalse);
    }

    static void containsPropertyTrue(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::containsPropertyTrue);
    }

    static void removePropertyTrue(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::removePropertyTrue);
    }

    static void removePropertyFalse(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::removePropertyFalse);
    }

    static void removePropertyAndGet(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::removePropertyAndGet);
    }

    static void removePropertyAndContains(AtomConstructor c) {
	forInstanceAndTemplate(c, AtomTester::removePropertyAndContains);
    }

    static void getInstancesZero(AtomConstructor c) {
	Template template = newTemplate(c);
	AtomTester.getInstancesZero(template);
    }

    static void getInstancesOne(AtomConstructor c) {
	Instance instance = newInstance(c);
	Template template = instance.getTemplate();
	AtomTester.getInstancesOne(template, instance);
    }

    static void getInstancesCountZero(AtomConstructor c) {
	Template template = newTemplate(c);
	AtomTester.getInstancesCountZero(template);
    }

    static void getInstancesCountOne(AtomConstructor c) {
	Instance instance = newInstance(c);
	AtomTester.getInstancesCountOne(instance);
    }

    private static void forInstanceAndTemplate(AtomConstructor c, Consumer<? super Atom> test) {
	Template template = newTemplate(c);
	Instance instance = newInstance(c);
	test.accept(template);
	test.accept(instance);
    }

    private static Template newTemplate(AtomConstructor c) {
	return c.newTemplate(ID.newID("table1"));
    }

    private static Instance newInstance(AtomConstructor c) {
	Template0 instanceTemplate = c.newTemplate(ID.newID("table2"));
	Instance0 instance = c.newInstance(ID.newID("table2"));
	instance.setTemplate(instanceTemplate.getID());
	instanceTemplate.addInstance(instance.getID());
	return instance;
    }

}
