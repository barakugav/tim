package com.barakugav.util.tim;

import java.util.Objects;

class ViewAtomAbstract implements ViewAtom {

    private final Atom0 atom;

    ViewAtomAbstract(Atom0 atom) {
	this.atom = Objects.requireNonNull(atom);
    }

    @Override
    public Atom0 atom() {
	return atom;
    }

}
