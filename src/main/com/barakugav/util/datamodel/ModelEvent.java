package com.barakugav.util.datamodel;

public class ModelEvent extends Event {

    ModelEvent(DataModel source) {
	super(source, null);
    }

    @Override
    public DataModel getSource() {
	return (DataModel) super.getSource();
    }

}
