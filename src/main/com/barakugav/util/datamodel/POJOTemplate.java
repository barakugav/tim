package com.barakugav.util.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class POJOTemplate extends POJOAtom implements Template0 {

    private static final String PROPERTY_INSTANCES = ".Instances";

    POJOTemplate(ID id) {
	super(id);
	setInstances(new ArrayList<Instance>(0));
    }

    @Override
    public boolean delete() {
	if (!super.delete())
	    return false;
	for (Instance instance : instances())
	    instance.delete();
	instances().clear();
	return true;
    }

    @Override
    public Collection<Instance> getInstances() {
	return Collections.unmodifiableCollection(instances());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Collection<Instance0> getInstances0() {
	return (Collection) instances();
    }

    boolean addInstance(Instance instance) {
	assert equals(instance.getTemplate());
	return instances().add(instance);
    }

    private Collection<Instance> instances() {
	return getProperty(PROPERTY_INSTANCES);
    }

    private boolean setInstances(Collection<Instance> instances) {
	return setProperty(PROPERTY_INSTANCES, instances);
    }

}
