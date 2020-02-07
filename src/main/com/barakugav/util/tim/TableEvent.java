package com.barakugav.util.tim;

public class TableEvent extends Event {

    TableEvent(ID data) {
	super(data.getTableName(), data);
    }

    @Override
    public String getSource() {
	return (String) super.getSource();
    }

    @Override
    public ID getData() {
	return (ID) super.getData();
    }

}
