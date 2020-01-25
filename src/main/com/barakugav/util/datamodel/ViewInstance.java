package com.barakugav.util.datamodel;

abstract class ViewInstance extends ViewAtom implements Instance0 {

    ViewInstance() {
    }

    @Override
    public Template0 getTemplate() {
	return atom().getTemplate();
    }

    @Override
    public void setTemplate(Template0 template) {
	atom().setTemplate(template);
    }

    @Override
    abstract Instance0 atom();

}
