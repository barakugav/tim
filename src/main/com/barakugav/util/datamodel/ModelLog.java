package com.barakugav.util.datamodel;

import java.util.Objects;

class ModelLog {

    static enum LogAction {
	Create, Change, Delete
    }

    private final ID source;
    private final LogAction action;
    private final String changeKey;
    private final String oldValue;
    private final String newValue;
    private final long timePoint;

    private ModelLog(ID source, LogAction action, String changeKey, String oldValue, String newValue) {
	this.source = Objects.requireNonNull(source);
	this.action = Objects.requireNonNull(action);
	if (LogAction.Change.equals(this.action)) {
	    this.changeKey = Objects.requireNonNull(changeKey);
	    this.oldValue = Objects.requireNonNull(oldValue);
	    this.newValue = Objects.requireNonNull(newValue);
	} else {
	    this.changeKey = null;
	    this.oldValue = null;
	    this.newValue = null;
	}
	timePoint = System.currentTimeMillis();
    }

    ID getSource() {
	return source;
    }

    LogAction getAction() {
	return action;
    }

    String getChangeKey() {
	return changeKey;
    }

    String getOldValue() {
	return oldValue;
    }

    String getNewValue() {
	return newValue;
    }

    long getTimePoint() {
	return timePoint;
    }

    static ModelLog newCreateLog(ID source) {
	return new ModelLog(source, LogAction.Create, null, null, null);
    }

    static ModelLog newDeleteLog(ID source) {
	return new ModelLog(source, LogAction.Delete, null, null, null);
    }

    static ModelLog newChangeLog(ID source, String changeKey, Object oldValue, Object newValue) {
	return new ModelLog(source, LogAction.Change, changeKey, String.valueOf(oldValue), String.valueOf(newValue));
    }

}
