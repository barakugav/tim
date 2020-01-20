package com.barakugav.util.datamodel;

import java.util.Collection;

public interface Template extends Atom {

    /**
     * Delete this template and all he's instances too
     */
    @Override
    public boolean delete();

    public Instance newInstance();

    public Collection<Instance> getInstances();
    
}
