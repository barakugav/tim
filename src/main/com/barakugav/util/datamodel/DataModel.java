package com.barakugav.util.datamodel;

public class DataModel {

    private final Model model;
    private final Storage storage;

    DataModel() {
	model = new ModelImpl();
	storage = new StorageXML();

	Runtime.getRuntime().addShutdownHook(new Thread() {

	    @Override
	    public void run() {
		storage.write(model);
	    }

	});
	storage.read(model);
    }

    public TemplateBuilder templateBuilder() {
	return model.templateBuilder();
    }

    public Template getTemplate(ID id) {
	return (Template) model.getAtom(id);
    }

    public Instance getInstance(ID id) {
	return (Instance) model.getAtom(id);
    }

}
