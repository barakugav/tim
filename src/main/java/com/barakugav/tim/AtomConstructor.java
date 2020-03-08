package com.barakugav.tim;

interface AtomConstructor {

    Template0 newTemplate(ID id);

    Instance0 newInstance(ID id);

    void setAtomResolver(AtomResolver resolver);

}
