package com.barakugav.tim.log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.barakugav.tim.ID;
import com.barakugav.tim.log.ModelLog.LogAction;

class ModelLogTest {

    @Test
    void newCreateLog() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog.newCreateLog(source);
    }

    @Test
    void createLogGetSource() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newCreateLog(source);
	assertEquals(source, log.getSource());
    }

    @Test
    void createLogGetAction() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newCreateLog(source);
	assertEquals(LogAction.Create, log.getAction());
    }

    @Test
    void createLogGetChangeKey() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newCreateLog(source);
	assertEquals(null, log.getChangeKey());
    }

    @Test
    void createLogGetOldValue() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newCreateLog(source);
	assertEquals(null, log.getOldValue());
    }

    @Test
    void createLogGetNewValue() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newCreateLog(source);
	assertEquals(null, log.getNewValue());
    }

    @Test
    void newDeleteLog() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog.newDeleteLog(source);
    }

    @Test
    void deleteLogGetSource() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newDeleteLog(source);
	assertEquals(source, log.getSource());
    }

    @Test
    void deleteLogGetAction() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newDeleteLog(source);
	assertEquals(LogAction.Delete, log.getAction());
    }

    @Test
    void deleteLogGetChangeKey() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newDeleteLog(source);
	assertEquals(null, log.getChangeKey());
    }

    @Test
    void deleteLogGetOldValue() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newDeleteLog(source);
	assertEquals(null, log.getOldValue());
    }

    @Test
    void deleteLogGetNewValue() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newDeleteLog(source);
	assertEquals(null, log.getNewValue());
    }

    @Test
    void newChangeLog() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog.newChangeLog(source, "k", "o", "n");
    }

    @Test
    void changeLogGetSource() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newChangeLog(source, "k", "o", "n");
	assertEquals(source, log.getSource());
    }

    @Test
    void changeLogGetAction() {
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newChangeLog(source, "k", "o", "n");
	assertEquals(LogAction.Change, log.getAction());
    }

    @Test
    void changeLogGetChangeKey() {
	String key = "k";
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newChangeLog(source, key, "o", "n");
	assertEquals(key, log.getChangeKey());
    }

    @Test
    void changeLogGetOldValue() {
	String old = "o";
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newChangeLog(source, "k", old, "n");
	assertEquals(old, log.getOldValue());
    }

    @Test
    void changeLogGetNewValue() {
	String newVal = "n";
	ID source = ID.newID("table1", ID.Type.Template);
	ModelLog log = ModelLog.newChangeLog(source, "k", "o", newVal);
	assertEquals(newVal, log.getNewValue());
    }

    @Test
    void getTimePoint() {
	ID source = ID.newID("table1", ID.Type.Template);
	long expectedLow = System.currentTimeMillis();
	ModelLog log = ModelLog.newCreateLog(source);
	long actual = log.getTimePoint();
	long expectedHigh = System.currentTimeMillis();
	assertTrue(expectedLow <= actual && actual <= expectedHigh);
    }

}
