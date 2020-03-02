package com.barakugav.tim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DTOTemplate extends DTOAtom {

    public final List<ID> instances;

    public DTOTemplate(Template template) {
	super(template);
	instances = Collections.unmodifiableList(new ArrayList<>(((Template0) template).getInstances0()));
    }

}
