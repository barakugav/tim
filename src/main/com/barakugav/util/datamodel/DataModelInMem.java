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

    @Override
    public Collection<Template> getTemplates(String tableName) {
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

    @Override
    public Collection<Instance> getInstances(String tableName) {
	Table table = tables.get(tableName);
	if (table == null)
	    return Collections.emptySet();
	return Collections.unmodifiableCollection(table.instance.values());
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
	eventManager.fireInstanceCreated(new TableEvent(id));
	return instance;
    }

    Template0 newEmptyTemplate(ID id) {
	Template0 template;

	// POJO level
	template = new POJOTemplate(id);
	// Logging level
	template = new LoggerTemplate(template, logger);
	// Evented level
	template = new EventedTemplate(template, eventManager);
	// Mem/Cache level
	template = new InMemTemplate(template);

	return template;
    }

    Instance0 newEmptyInstance(ID id) {
	Instance0 instance;

	// POJO level
	instance = new POJOInstance(id);
	// Logging level
	instance = new LoggerInstance(instance, logger);
	// Evented level
	instance = new EventedInstance(instance, eventManager);
	// Mem/Cache level
	instance = new InMemInstance(instance);

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
	    table.instance.clear();
	}
	tables.clear();

	isOpen = false;
	return true;
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
