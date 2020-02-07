package com.barakugav.util.tim;

interface ViewInstance extends ViewAtom, Instance0 {

    @Override
    default Template0 getTemplate() {
	return atom().getTemplate();
    }

    @Override
    default boolean setTemplate(Template0 template) {
	return atom().setTemplate(template);
    }

    @Override
    abstract Instance0 atom();

}
