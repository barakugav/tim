package com.barakugav.util.datamodel;

import java.util.Collection;

abstract class ViewTemplate extends ViewAtom implements Template0 {

    ViewTemplate() {
	super();
    }

    @Override
    public Collection<Instance> getInstances() {
	return atom().getInstances();
    }

    @Override
    public Collection<Instance0> getInstances0() {
	return atom().getInstances0();
    }

    @Override
    abstract Template0 atom();

}
