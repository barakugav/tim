package com.barakugav.tim;

import java.util.Collection;

interface Template0 extends Atom0, Template {

    Collection<ID> getInstances0();

    boolean addInstance(ID instance);

    boolean removeInstance(ID instance);

    boolean setInstances(Collection<ID> instances);

}
