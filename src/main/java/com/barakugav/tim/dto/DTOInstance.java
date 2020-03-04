package com.barakugav.tim.dto;

import com.barakugav.tim.ID;
import com.barakugav.tim.Instance;

public class DTOInstance extends DTOAtom {

    public final ID template;

    public DTOInstance(Instance instance) {
	super(instance);
	template = instance.getTemplate().getID();
    }

}
