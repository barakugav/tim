package com.barakugav.util.datamodel;

import java.util.Collection;

interface ViewTemplate extends ViewAtom, Template0 {

    @Override
    default Collection<Instance> getInstances() {
	return atom().getInstances();
    }

    @Override
    default Collection<Instance0> getInstances0() {
	return atom().getInstances0();
    }

    @Override
    Template0 atom();

}
