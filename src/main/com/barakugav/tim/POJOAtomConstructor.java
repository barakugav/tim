package com.barakugav.tim;

import java.util.Objects;

class POJOAtomConstructor implements AtomConstructor {

    private AtomResolver resolver;

    POJOAtomConstructor() {
    }

    void setAtomResolver(AtomResolver resolver) {
	this.resolver = Objects.requireNonNull(resolver);
    }

    @Override
    public Template0 newTemplate(ID id) {
	return newTemplate0(id);
    }

    @Override
    public Instance0 newInstance(ID id) {
	return newInstance0(id);
    }

    Template0 newTemplate0(ID id) {
	return new POJOTemplate(id, resolver);
    }

    Instance0 newInstance0(ID id) {
	return new POJOInstance(id, resolver);
    }

}
