package com.barakugav.tim;

abstract class AbstractViewTemplate extends AbstractViewAtom implements ViewTemplate {

    AbstractViewTemplate(Template0 template) {
	super(template);
    }

    @Override
    public Template0 atom() {
	return (Template0) super.atom();
    }

}
