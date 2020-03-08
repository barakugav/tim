package com.barakugav.tim;

class AtomConstructorAbstractViewAtoms implements AtomConstructor {

    private final AtomConstructor a;

    AtomConstructorAbstractViewAtoms() {
	a = new AtomConstructorPOJOWithInternalResolver() {

	    @Override
	    Template0 newTemplate0(ID id) {
		return new AbstractViewTemplate(super.newTemplate0(id)) {
		};
	    }

	    @Override
	    Instance0 newInstance0(ID id) {
		return new AbstractViewInstance(super.newInstance0(id)) {
		};
	    }

	};
    }

    @Override
    public Template0 newTemplate(ID id) {
	return a.newTemplate(id);
    }

    @Override
    public Instance0 newInstance(ID id) {
	return a.newInstance(id);
    }

    @Override
    public void setAtomResolver(AtomResolver resolver) {
	a.setAtomResolver(resolver);
    }

}
