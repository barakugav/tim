package com.barakugav.util.datamodel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

abstract class StorageInMem implements Storage {

    private final Map<String, Table> tables;

    StorageInMem() {
	tables = new HashMap<>();
    }

    @Override
    public Collection<String> tableNames() {
	return Collections.unmodifiableSet(tables.keySet());
    }

    @Override
    public Collection<Template> getTemplates(String tableName) {
	Table table = tables.get(tableName);
	if (table == null)
	    return Collections.emptySet();
	return Collections.unmodifiableCollection(table.templates.values());
    }

    @Override
    public Collection<Instance> getInstances(String tableName) {
	Table table = tables.get(tableName);
	if (table == null)
	    return Collections.emptySet();
	return Collections.unmodifiableCollection(table.instance.values());
    }

    @Override
    public Template getTemplate(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	return table.templates.get(id);
    }

    @Override
    public Instance getIntance(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	return table.instance.get(id);
    }

    @Override
    public Template0 newTemplate(String tableName) {
	ID id = ID.newID(tableName);
	return newEmptyTemplate(id);
    }

    @Override
    public Instance0 newInstance(Template template) {
	String tableName = template.getID().getTableName();
	ID id = ID.newID(tableName);
	Instance0 instance = newEmptyInstance(id);
	instance.setTemplate((Template0) template);
	return instance;
    }

    Template0 newEmptyTemplate(ID id) {
	Template0 result;

	// POJO level
	result = new POJOTemplate(id);
	// Mem/Cache level
	result = new InMemTemplate(result);

	return result;
    }

    Instance0 newEmptyInstance(ID id) {
	Instance0 result;

	// POJO level
	result = new POJOInstance(id);
	// Mem/Cache level
	result = new InMemInstance(result);

	return result;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {
	for (Table table : tables.values()) {
	    table.templates.clear();
	    table.instance.clear();
	}
	tables.clear();
    }

    boolean isInMemory(ID id) {
	Table table = tables.get(id.getTableName());
	return table.templates.containsKey(id) || table.instance.containsKey(id);
    }

    private void addToMem(Atom atom) {
	ID id = atom.getID();
	String tableName = id.getTableName();
	Table table = tables.computeIfAbsent(tableName, n -> new Table());
	Atom previous = null;
	if (atom instanceof Template)
	    previous = table.templates.put(id, (Template) atom);
	else if (atom instanceof Instance)
	    previous = table.instance.put(id, (Instance) atom);
	else
	    throw new IllegalArgumentException("Unkown atom type: " + atom.getClass());
	if (previous != null)
	    throw new IllegalArgumentException("Atom with same ID already exists: " + id);
    }

    private boolean removeFromMem(Atom atom) {
	ID id = atom.getID();
	String tableName = id.getTableName();
	Table table = tables.get(tableName);
	if (table == null)
	    return false;
	if (atom instanceof Template)
	    return table.templates.remove(id, atom);
	if (atom instanceof Instance)
	    return table.instance.remove(id, atom);
	throw new IllegalArgumentException("Unkown atom type: " + atom.getClass());
    }

    private static class Table {

	final Map<ID, Template> templates;
	final Map<ID, Instance> instance;

	Table() {
	    templates = new HashMap<>();
	    instance = new HashMap<>();
	}

    }

    private class InMemTemplate extends ViewTemplate {

	private final Template0 template;

	InMemTemplate(Template0 template) {
	    this.template = Objects.requireNonNull(template);
	    addToMem(this);
	}

	@Override
	public boolean delete() {
	    if (!super.delete())
		return false;
	    removeFromMem(this);
	    return true;
	}

	@Override
	Template0 atom() {
	    return template;
	}

    }

    private class InMemInstance extends ViewInstance {

	private final Instance0 instance;

	InMemInstance(Instance0 instance) {
	    this.instance = Objects.requireNonNull(instance);
	    addToMem(this);
	}

	@Override
	public boolean delete() {
	    if (!super.delete())
		return false;
	    removeFromMem(this);
	    return true;
	}

	@Override
	Instance0 atom() {
	    return instance;
	}

    }

}
