package com.barakugav.util.datamodel;

class LoggerTemplate extends LoggerAtom implements ViewTemplate {

    LoggerTemplate(Template0 template, ModelLogger logger) {
	super(template, logger);
    }

    @Override
    public Template0 atom() {
	return (Template0) super.atom();
    }

}
