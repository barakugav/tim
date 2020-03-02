package com.barakugav.tim;

abstract class AbstractViewInstance extends AbstractViewAtom implements ViewInstance {

    AbstractViewInstance(Instance0 template) {
	super(template);
    }

    @Override
    public Instance0 atom() {
	return (Instance0) super.atom();
    }

}
