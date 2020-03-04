package com.barakugav.tim.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.barakugav.tim.Atom;
import com.barakugav.tim.ID;
import com.barakugav.tim.Instance;
import com.barakugav.tim.Template;

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

    public static DTOAtom valueOf(Atom atom) {
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
