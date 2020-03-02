package com.barakugav.tim;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AbstractViewAtomTest {

    @Test
    void createAtomConstructorPOJO() {
	assertNotNull(newAtomConstructor());
    }

    @Test
    void createTemplate() {
	AtomConstructorTester.createTemplate(newAtomConstructor());
    }

    @Test
    void createInstance() {
	AtomConstructorTester.createInstance(newAtomConstructor());
    }

    @Test
    void getId() {
	AtomConstructorTester.getId(newAtomConstructor());
    }

    @Test
    void getTemplate() {
	AtomConstructorTester.getTemplate(newAtomConstructor());
    }

    @Test
    void setTemplate() {
	AtomConstructorTester.setTemplate(newAtomConstructor());
    }

    @Test
    void isAlive() {
	AtomConstructorTester.isAlive(newAtomConstructor());
    }

    @Test
    void delete() {
	AtomConstructorTester.delete(newAtomConstructor());
    }

    @Test
    void deleteAndIsAliveFalse() {
	AtomConstructorTester.deleteAndIsAliveFalse(newAtomConstructor());
    }

    @Test
    void deleteTwise() {
	AtomConstructorTester.deleteTwise(newAtomConstructor());

    }

    @Test
    void deleteTemplate() {
	AtomConstructorTester.deleteTemplate(newAtomConstructor());
    }

    @Test
    void getPropertyEmpty() {
	AtomConstructorTester.getPropertyEmpty(newAtomConstructor());
    }

    @Test
    void getPropertyNullKey() {
	AtomConstructorTester.getPropertyNullKey(newAtomConstructor());
    }

    @Test
    void getPropertiesEmpty() {
	AtomConstructorTester.getPropertiesEmpty(newAtomConstructor());
    }

    @Test
    void getPropertiesNonEmpty() {
	AtomConstructorTester.getPropertiesNonEmpty(newAtomConstructor());
    }

    @Test
    void setProperty() {
	AtomConstructorTester.setProperty(newAtomConstructor());
    }

    @Test
    void setPropertyAndGetString() {
	AtomConstructorTester.setPropertyAndGetString(newAtomConstructor());
    }

    @Test
    void setPropertyAndGetNumber() {
	AtomConstructorTester.setPropertyAndGetNumber(newAtomConstructor());
    }

    @Test
    void setPropertyAndGetSet() {
	AtomConstructorTester.setPropertyAndGetSet(newAtomConstructor());
    }

    @Test
    void containsPropertyFalse() {
	AtomConstructorTester.containsPropertyFalse(newAtomConstructor());
    }

    @Test
    void containsPropertyTrue() {
	AtomConstructorTester.containsPropertyTrue(newAtomConstructor());
    }

    @Test
    void removePropertyTrue() {
	AtomConstructorTester.removePropertyTrue(newAtomConstructor());
    }

    @Test
    void removePropertyFalse() {
	AtomConstructorTester.removePropertyFalse(newAtomConstructor());
    }

    @Test
    void removePropertyAndGet() {
	AtomConstructorTester.removePropertyAndGet(newAtomConstructor());
    }

    @Test
    void removePropertyAndContains() {
	AtomConstructorTester.removePropertyAndContains(newAtomConstructor());
    }

    @Test
    void getInstancesZero() {
	AtomConstructorTester.getInstancesZero(newAtomConstructor());
    }

    @Test
    void getInstancesOne() {
	AtomConstructorTester.getInstancesOne(newAtomConstructor());
    }

    @Test
    void getInstancesCountZero() {
	AtomConstructorTester.getInstancesCountZero(newAtomConstructor());
    }

    @Test
    void getInstancesCountOne() {
	AtomConstructorTester.getInstancesCountOne(newAtomConstructor());
    }

    private static AtomConstructor newAtomConstructor() {
	return new AtomConstructorAbstractViewAtoms();
    }

}
