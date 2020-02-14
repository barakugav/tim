package com.barakugav.tim;

import java.util.Collection;

public interface Template extends Atom {

    public Collection<Instance> getInstances();

    public int getInstanceCount();

}
