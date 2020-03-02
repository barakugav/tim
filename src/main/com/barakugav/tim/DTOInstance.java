package com.barakugav.tim;

public class DTOInstance extends DTOAtom {

    public final ID template;

    public DTOInstance(Instance instance) {
	super(instance);
	template = ((Instance0) instance).getTemplate0();
    }

}
