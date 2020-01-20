package com.barakugav.util.datamodel;

import java.util.Collection;

interface Model {

    Collection<Template> templates();

    Collection<Instance> instances();

    Atom getAtom(ID id);

    boolean addAtom(Atom atom);

    boolean removeAtom(Atom atom);

    TemplateBuilder templateBuilder();

}
