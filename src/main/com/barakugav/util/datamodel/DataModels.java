package com.barakugav.util.datamodel;

public class DataModels {

    private DataModels() {
	throw new InternalError();
    }

    public static DataModel getDefaultModel() {
	return new DataModelXML();
    }

}
