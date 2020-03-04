package com.barakugav.tim.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.barakugav.tim.ID;
import com.barakugav.tim.Instance;
import com.barakugav.tim.Template;

public class DTOTemplate extends DTOAtom {

    public final List<ID> instances;

    public DTOTemplate(Template template) {
	super(template);
	Collection<Instance> il = template.getInstances();
	List<ID> l = new ArrayList<>(il.size());
	for (Instance i : il)
	    l.add(i.getID());
	instances = Collections.unmodifiableList(l);
    }

}
