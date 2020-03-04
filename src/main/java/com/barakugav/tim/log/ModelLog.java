package com.barakugav.tim.log;

import java.util.Objects;

import com.barakugav.tim.ID;

public class ModelLog {

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

    public ID getSource() {
	return source;
    }

    public LogAction getAction() {
	return action;
    }

    public String getChangeKey() {
	return changeKey;
    }

    public String getOldValue() {
	return oldValue;
    }

    public String getNewValue() {
	return newValue;
    }

    public long getTimePoint() {
	return timePoint;
    }

    public static ModelLog newCreateLog(ID source) {
	return new ModelLog(source, LogAction.Create, null, null, null);
    }

    public static ModelLog newDeleteLog(ID source) {
	return new ModelLog(source, LogAction.Delete, null, null, null);
    }

    public static ModelLog newChangeLog(ID source, String changeKey, Object oldValue, Object newValue) {
	return new ModelLog(source, LogAction.Change, changeKey, String.valueOf(oldValue), String.valueOf(newValue));
    }

}
