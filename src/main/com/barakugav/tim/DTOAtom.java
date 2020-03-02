package com.barakugav.tim;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class DTOAtom {

    public final ID id;
    public final Map<String, Object> properties;
    public final boolean isAlive;
    public final long version;

    public DTOAtom(Atom atom) {
	id = atom.getID();
	properties = Collections.unmodifiableMap(new HashMap<>(atom.getProperties()));
	isAlive = atom.isAlive();
	version = atom.getVersion();
    }

    static DTOAtom valueOf(Atom atom) {
	if (atom == null || !atom.isAlive())
	    return null;
	if (atom instanceof Template)
	    return new DTOTemplate((Template) atom);
	else if (atom instanceof Instance)
	    return new DTOInstance((Instance) atom);
	else
	    throw new IllegalArgumentException();
    }

}
