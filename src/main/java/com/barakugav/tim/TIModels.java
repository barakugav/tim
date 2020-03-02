package com.barakugav.tim;

public class TIModels {

    private TIModels() {
	throw new InternalError();
    }

    public static TIModel getDefaultModel() {
	return new TIMXML();
    }

}
