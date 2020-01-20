package com.barakugav.util.datamodel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ModelImpl implements Model {

    private final Map<ID, Template> templates;
    private final Map<ID, Instance> instances;

    ModelImpl() {
	templates = new HashMap<>();
	instances = new HashMap<>();
    }

    @Override
    public Collection<Template> templates() {
	return Collections.unmodifiableCollection(templates.values());
    }

    @Override
    public Collection<Instance> instances() {
	return Collections.unmodifiableCollection(instances.values());
    }

    @Override
    public Atom getAtom(ID id) {
	Atom atom = templates.get(id);
	if (atom == null)
	    atom = instances.get(id);
	return atom;
    }

    @Override
    public boolean addAtom(Atom atom) {
	if (atom instanceof Template) {
	    if (templates.put(atom.getID(), (Template) atom) != null)
		throw new InternalError();
	} else if (atom instanceof Instance) {
	    if (instances.put(atom.getID(), (Instance) atom) != null)
		throw new InternalError();
	} else {
	    throw new IllegalArgumentException("Unknown atom type: " + atom.getClass());
	}
	return true;
    }

    @Override
    public boolean removeAtom(Atom atom) {
	if (atom instanceof Template)
	    return templates.remove(atom.getID(), atom);
	if (atom instanceof Instance)
	    return instances.remove(atom.getID(), atom);
	throw new IllegalArgumentException("Unknown atom type: " + atom.getClass());
    }

    @Override
    public TemplateBuilder templateBuilder() {
	return new TemplateBuilderImpl();
    }

    private class TemplateBuilderImpl implements TemplateBuilder {

	@Override
	public Template newTemplate() {
	    return new TemplateImpl(ModelImpl.this);
	}

    }

}
