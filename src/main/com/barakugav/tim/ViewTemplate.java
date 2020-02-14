package com.barakugav.tim;

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
    default int getInstanceCount() {
	return atom().getInstanceCount();
    }

    @Override
    default boolean addInstance(Instance0 instance) {
	return atom().addInstance(instance);
    }

    @Override
    default boolean setInstances(Collection<Instance0> instances) {
	return atom().setInstances(instances);
    }

    @Override
    Template0 atom();

}
