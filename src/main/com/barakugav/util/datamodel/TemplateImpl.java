package com.barakugav.util.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

class TemplateImpl extends AtomImpl implements Template {

    private static final String PROPERTY_INSTANCES = ".Instances";

    TemplateImpl(Model model) {
	super(model);
	setInstances(new ArrayList<ID>(0));
    }

    TemplateImpl(Model model, ID id, Map<String, Object> properties) {
	super(model, id, properties);
    }

    @Override
    public boolean delete() {
	if (!super.delete())
	    return false;
	for (ID instance : instances())
	    getModel().getAtom(instance).delete();
	instances().clear();
	return true;
    }

    @Override
    public Instance newInstance() {
	Instance instance = new InstanceImpl(getModel(), this);
	instances().add(instance.getID());
	return instance;
    }

    @Override
    public Collection<Instance> getInstances() {
	Collection<ID> instancesIDs = instances();
	Collection<Instance> instances = new ArrayList<>(instancesIDs.size());
	for (ID instanceID : instancesIDs)
	    instances.add((Instance) getModel().getAtom(instanceID));
	return instances;
    }

    private Collection<ID> instances() {
	return getProperty(PROPERTY_INSTANCES);
    }

    private boolean setInstances(Collection<ID> instances) {
	return setProperty(PROPERTY_INSTANCES, instances);
    }

}
