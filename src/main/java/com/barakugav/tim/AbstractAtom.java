package com.barakugav.tim;

abstract class AbstractAtom implements Atom {

    @Override
    public final boolean equals(Object other) {
	if (other == this)
	    return true;
	if (!(other instanceof Atom))
	    return false;

	Atom o = (Atom) other;
	return o.getID().equals(getID());
    }

    @Override
    public final int hashCode() {
	return getID().hashCode();
    }

    @Override
    public String toString() {
	return "Atom[" + getID() + "]";
    }

}
