package com.barakugav.tim;

public class ModelEvent extends Event {

    ModelEvent(TIModel source) {
	super(source, null);
    }

    @Override
    public TIModel getSource() {
	return (TIModel) super.getSource();
    }

}
