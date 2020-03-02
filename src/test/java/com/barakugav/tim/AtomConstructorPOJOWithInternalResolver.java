package com.barakugav.tim;

import java.util.HashMap;
import java.util.Map;

class AtomConstructorPOJOWithInternalResolver extends POJOAtomConstructor {

    private final Map<ID, Atom> atoms;

    AtomConstructorPOJOWithInternalResolver() {
	super();
	atoms = new HashMap<>();
	setAtomResolver(new ResolverImpl());
    }

    @Override
    public Template0 newTemplate(ID id) {
	return cache(super.newTemplate(id));
    }

    @Override
    public Instance0 newInstance(ID id) {
	return cache(super.newInstance(id));
    }

    private <A extends Atom> A cache(A a) {
	atoms.put(a.getID(), a);
	return a;
    }

    private class ResolverImpl implements AtomResolver {

	@Override
	public Instance0 getInstance(ID id) {
	    return (Instance0) atoms.get(id);
	}

	@Override
	public Template0 getTemplate(ID id) {
	    return (Template0) atoms.get(id);
	}

    }

}
