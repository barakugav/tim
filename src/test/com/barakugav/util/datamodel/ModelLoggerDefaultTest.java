package com.barakugav.util.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ModelLoggerDefaultTest {

    @SuppressWarnings("unused")
    @Test
    void createLogger() {
	new ModelLoggerDefault();
    }

    @Test
    void logCreateLog() {
	ModelLogger logger = new ModelLoggerDefault();
	ModelLog log = ModelLog.newCreateLog(ID.newID("table1"));
	logger.log(log);
    }

    @Test
    void logDeleteLog() {
	ModelLogger logger = new ModelLoggerDefault();
	ModelLog log = ModelLog.newDeleteLog(ID.newID("table1"));
	logger.log(log);
    }

    @Test
    void logChangeLog() {
	ModelLogger logger = new ModelLoggerDefault();
	ModelLog log = ModelLog.newChangeLog(ID.newID("table1"), "k", "o", "n");
	logger.log(log);
    }

    @Test
    void getLogsEmptyLogger() {
	ModelLogger logger = new ModelLoggerDefault();
	logger.getLogs(0, Long.MAX_VALUE);
    }

    @Test
    void getLogsEmptyLoggerIteratorEmpty() {
	ModelLogger logger = new ModelLoggerDefault();
	Iterator<ModelLog> logs = logger.getLogs(0, Long.MAX_VALUE);
	assertFalse(logs.hasNext());
    }

    @Test
    void getLogsAll6Logs() {
	Set<ModelLog> expected = new HashSet<>();
	ModelLog log1 = ModelLog.newCreateLog(ID.newID("table1"));
	ModelLog log2 = ModelLog.newCreateLog(ID.newID("table2"));
	ModelLog log3 = ModelLog.newDeleteLog(ID.newID("table1"));
	ModelLog log4 = ModelLog.newDeleteLog(ID.newID("table2"));
	ModelLog log5 = ModelLog.newChangeLog(ID.newID("table1"), "k", "o", "n");
	ModelLog log6 = ModelLog.newChangeLog(ID.newID("table2"), "k", "o", "n");
	expected.addAll(Arrays.asList(log1, log2, log3, log4, log5, log6));

	ModelLogger logger = new ModelLoggerDefault();
	logger.log(log1);
	logger.log(log2);
	logger.log(log3);
	logger.log(log4);
	logger.log(log5);
	logger.log(log6);
	Iterator<ModelLog> logs = logger.getLogs(0, Long.MAX_VALUE);
	Set<ModelLog> actual = new HashSet<>();
	for (; logs.hasNext();)
	    actual.add(logs.next());

	assertEquals(expected, actual);
    }

    @Test
    void getLogsPartialLogs() throws InterruptedException {
	Set<ModelLog> expected = new HashSet<>();
	ModelLog log1 = ModelLog.newCreateLog(ID.newID("table1"));
	ModelLog log2 = ModelLog.newCreateLog(ID.newID("table2"));
	Thread.sleep(5);
	long queryLow = System.currentTimeMillis();
	Thread.sleep(5);
	ModelLog log3 = ModelLog.newDeleteLog(ID.newID("table1"));
	ModelLog log4 = ModelLog.newDeleteLog(ID.newID("table2"));
	ModelLog log5 = ModelLog.newChangeLog(ID.newID("table1"), "k", "o", "n");
	Thread.sleep(5);
	long queryHigh = System.currentTimeMillis();
	Thread.sleep(5);
	ModelLog log6 = ModelLog.newChangeLog(ID.newID("table2"), "k", "o", "n");
	expected.addAll(Arrays.asList(log3, log4, log5));

	ModelLogger logger = new ModelLoggerDefault();
	logger.log(log1);
	logger.log(log2);
	logger.log(log3);
	logger.log(log4);
	logger.log(log5);
	logger.log(log6);
	Iterator<ModelLog> logs = logger.getLogs(queryLow, queryHigh);
	Set<ModelLog> actual = new HashSet<>();
	for (; logs.hasNext();)
	    actual.add(logs.next());

	assertEquals(expected, actual);
    }

}
