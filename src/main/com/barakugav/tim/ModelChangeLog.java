package com.barakugav.tim;

public class ModelChangeLog extends ChangeLog {

    ModelChangeLog(String before, String after) {
	super(before, after);
    }

    @Override
    public String getBefore() {
	return (String) super.getBefore();
    }

    @Override
    public String getAfter() {
	return (String) super.getAfter();
    }

}
