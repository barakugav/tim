package com.barakugav.util.datamodel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class POJOTemplate extends POJOAtom implements Template0 {

    private final Set<Instance0> instances;

    POJOTemplate(ID id) {
	super(id);
	instances = new HashSet<>(0);
    }

    @Override
    public boolean delete() {
	if (!super.delete())
	    return false;
	for (Instance instance : instances)
	    instance.delete();
	instances.clear();
	return true;
    }

    @Override
    public Collection<Instance> getInstances() {
	return Collections.unmodifiableCollection(instances);
    }

    @Override
    public Collection<Instance0> getInstances0() {
	return instances;
    }

    @Override
    public boolean addInstance(Instance0 instance) {
	assert equals(instance.getTemplate());
	return instances.add(instance);
    }

    @Override
    public boolean setInstances(Collection<Instance0> instances) {
	if (instances == null)
	    instances = Collections.emptyList();
	if (instances.equals(this.instances))
	    return false;
	this.instances.clear();
	this.instances.addAll(instances);
	return true;
    }

}
