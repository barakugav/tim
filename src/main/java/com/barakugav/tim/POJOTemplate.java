package com.barakugav.tim;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class POJOTemplate extends POJOAtom implements Template0 {

    private final Set<ID> instances;

    POJOTemplate(ID id, AtomResolver resolver) {
	super(id, resolver);
	instances = new HashSet<>(0);
    }

    @Override
    public boolean delete() {
	if (!super.delete())
	    return false;
	for (ID instance : instances)
	    resolver.apply(instance).delete();
	instances.clear();
	return true;
    }

    @Override
    public Collection<Instance> getInstances() {
	return instances.stream().map(id -> (Instance) resolver.apply(id)).collect(Collectors.toList());
    }

    @Override
    public Collection<ID> getInstances0() {
	return Collections.unmodifiableSet(instances);
    }

    @Override
    public int getInstanceCount() {
	return instances.size();
    }

    @Override
    public boolean addInstance(ID instance) {
	assert getID().equals(resolver.getInstance(instance).getTemplate0());
	if (!instances.add(instance))
	    return false;
	version++;
	return true;
    }
    
    @Override
    public boolean removeInstance(ID instance) {
	if (!instances.remove(instance))
	    return false;
	version++;
	return true;
    }

    @Override
    public boolean setInstances(Collection<ID> instances) {
	if (instances == null)
	    instances = Collections.emptyList();
	if (instances.equals(this.instances))
	    return false;
	this.instances.clear();
	this.instances.addAll(instances);
	version++;
	return true;
    }

}
