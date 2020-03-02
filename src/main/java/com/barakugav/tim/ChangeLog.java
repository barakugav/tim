package com.barakugav.tim;

public class ChangeLog {

    private final Object before;
    private final Object after;

    ChangeLog(Object before, Object after) {
	this.before = before;
	this.after = after;
    }

    public Object getBefore() {
	return before;
    }

    public Object getAfter() {
	return after;
    }

}
