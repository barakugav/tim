package com.barakugav.util.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

abstract class DataModelInMem implements DataModel {

    private final Map<String, Table> tables;
    private final ModelLogger logger;
    private final EventManager eventManager;

    private boolean isOpen;

    DataModelInMem() {
	tables = new HashMap<>();
	logger = new ModelLoggerDefault();
	eventManager = new EventManager();

	isOpen = false;
    }

    @Override
    public Collection<String> tableNames() {
	return Collections.unmodifiableSet(tables.keySet());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Collection<Template> getTemplates(String tableName) {
	return (Collection) getTemplates0(tableName);
    }

    Collection<Template0> getTemplates0(String tableName) {
	Table table = tables.get(tableName);
	if (table == null)
	    return Collections.emptySet();
	return Collections.unmodifiableCollection(table.templates.values());
    }

    @Override
    public Collection<Template> getTemplates(String tableName, Predicate<Template> condition) {
	List<Template> result = new ArrayList<>();
	for (Template template : getTemplates(tableName))
	    if (condition.test(template))
		result.add(template);
	return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Collection<Instance> getInstances(String tableName) {
	return (Collection) getInstances0(tableName);
    }

    Collection<Instance0> getInstances0(String tableName) {
	Table table = tables.get(tableName);
	if (table == null)
	    return Collections.emptySet();
	return Collections.unmodifiableCollection(table.instances.values());
    }

    @Override
    public Collection<Instance> getInstances(String tableName, Predicate<Instance> condition) {
	List<Instance> result = new ArrayList<>();
	for (Instance instance : getInstances(tableName))
	    if (condition.test(instance))
		result.add(instance);
	return result;
    }

    @Override
    public Template0 getTemplate(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	return table.templates.get(id);
    }

    @Override
    public Instance0 getIntance(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	return table.instances.get(id);
    }

    @Override
    public Template0 newTemplate(String tableName) {
	ID id = ID.newID(tableName);
	Template0 template = newEmptyTemplate(id);
	eventManager.fireTemplateCreated(new TableEvent(id));
	return template;
    }

    @Override
    public Instance0 newInstance(Template template) {
	String tableName = template.getID().getTableName();
	ID id = ID.newID(tableName);
	Instance0 instance = newEmptyInstance(id);
	instance.setTemplate((Template0) template);
	((Template0) template).addInstance(instance);
	eventManager.fireInstanceCreated(new TableEvent(id));
	return instance;
    }

    private Template0 newEmptyTemplate(ID id) {
	Template0 template;

	// POJO level
	template = new POJOTemplate(id);
	// Logging level
	template = new LoggerTemplate(template, logger);
	// Evented level
	template = new EventedTemplate(template, eventManager);
	// Mem/Cache level (Must be the last)
	template = new InMemTemplate(template);

	return template;
    }

    private Instance0 newEmptyInstance(ID id) {
	Instance0 instance;

	// POJO level
	instance = new POJOInstance(id);
	// Logging level
	instance = new LoggerInstance(instance, logger);
	// Evented level
	instance = new EventedInstance(instance, eventManager);
	// Mem/Cache level (Must be the last)
	instance = new InMemInstance(instance);

	return instance;
    }

    Template0 getOrCreateEmptyTemplate(ID id) {
	Template0 template = getTemplate(id);
	if (template == null)
	    template = newEmptyTemplate(id);
	return template;
    }

    Instance0 getOrCreateEmptyInstance(ID id) {
	Instance0 instance = getIntance(id);
	if (instance == null)
	    instance = newEmptyInstance(id);
	return instance;
    }

    @Override
    public EventManager getEventManager() {
	return eventManager;
    }

    @Override
    public Collection<ID> getChangedAtoms(long begin, long end) {
	if (logger == null)
	    throw new UnsupportedOperationException();
	Iterator<ModelLog> logs = logger.getLogs(begin, end);
	Collection<ID> result = new HashSet<>();
	for (; logs.hasNext();)
	    result.add(logs.next().getSource());
	return result;
    }

    @Override
    public boolean isOpen() {
	return isOpen;
    }

    @Override
    public final void open() {
	if (open0())
	    eventManager.fireModelOpened(new ModelEvent(this));
    }

    boolean open0() {
	if (isOpen())
	    return false;

	// Nothing to do here

	isOpen = true;
	return true;
    }

    @Override
    public final void close() {
	if (isOpen())
	    eventManager.fireModelBeforeClose(new ModelEvent(this));
	if (close0())
	    eventManager.fireModelClosed(new ModelEvent(this));
    }

    boolean close0() {
	if (!isOpen())
	    return false;

	for (Table table : tables.values()) {
	    table.templates.clear();
	    table.instances.clear();
	}
	tables.clear();

	isOpen = false;
	return true;
    }

    boolean isInMemory(ID id) {
	Table table = tables.get(id.getTableName());
	return table.templates.containsKey(id) || table.instances.containsKey(id);
    }

    private void addToMem(Atom0 atom) {
	ID id = atom.getID();
	String tableName = id.getTableName();
	Table table = tables.computeIfAbsent(tableName, n -> new Table());
	Atom previous = null;
	if (atom instanceof Template0)
	    previous = table.templates.put(id, (Template0) atom);
	else if (atom instanceof Instance0)
	    previous = table.instances.put(id, (Instance0) atom);
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
	    return table.instances.remove(id, atom);
	throw new IllegalArgumentException("Unkown atom type: " + atom.getClass());
    }

    private static class Table {

	final Map<ID, Template0> templates;
	final Map<ID, Instance0> instances;

	Table() {
	    templates = new HashMap<>();
	    instances = new HashMap<>();
	}

    }

    private class InMemAtom extends ViewAtomAbstract {

	InMemAtom(Atom0 atom) {
	    super(atom);
	    addToMem(this);
	}

	@Override
	public boolean delete() {
	    if (!atom().delete())
		return false;
	    removeFromMem(this);
	    return true;
	}

    }

    private class InMemTemplate extends InMemAtom implements ViewTemplate {

	InMemTemplate(Template0 template) {
	    super(template);
	}

	@Override
	public Template0 atom() {
	    return (Template0) super.atom();
	}

    }

    private class InMemInstance extends InMemAtom implements ViewInstance {

	InMemInstance(Instance0 instance) {
	    super(instance);
	}

	@Override
	public Instance0 atom() {
	    return (Instance0) super.atom();
	}

    }

}
