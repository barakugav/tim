package com.barakugav.tim;

import java.util.Objects;

abstract class AbstractViewAtom extends AbstractAtom implements ViewAtom {

    private final Atom0 atom;

    AbstractViewAtom(Atom0 atom) {
	this.atom = Objects.requireNonNull(atom);
    }

    @Override
    public Atom0 atom() {
	return atom;
    }

}
