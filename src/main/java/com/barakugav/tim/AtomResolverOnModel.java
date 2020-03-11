package com.barakugav.tim;

import java.util.Objects;

class AtomResolverOnModel implements AtomResolver {

    private final TIModel model;

    AtomResolverOnModel(TIModel model) {
	this.model = Objects.requireNonNull(model);
    }

    @Override
    public Instance0 getInstance(ID id) {
	return (Instance0) model.getIntance(id);
    }

    @Override
    public Template0 getTemplate(ID id) {
	return (Template0) model.getTemplate(id);
    }

    @Override
    public Atom apply(ID id) {
	return model.getAtom(id);
    }

}
