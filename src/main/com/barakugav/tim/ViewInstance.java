package com.barakugav.tim;

interface ViewInstance extends ViewAtom, Instance0 {

    @Override
    default Template0 getTemplate() {
	return atom().getTemplate();
    }

    @Override
    default ID getTemplate0() {
	return atom().getTemplate0();
    }

    @Override
    default boolean setTemplate(ID template) {
	return atom().setTemplate(template);
    }

    @Override
    abstract Instance0 atom();

}
