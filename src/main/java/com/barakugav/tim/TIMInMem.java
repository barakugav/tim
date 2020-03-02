package com.barakugav.tim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import com.barakugav.event.EventCenter;
import com.barakugav.event.EventCunsumer;
import com.barakugav.event.EventProducer;

abstract class TIMInMem implements TIModel {

    private final String name;

    private final Map<String, Table> tables;
    private final AtomResolver resolver;

    private final ModelLogger logger;
    private final EventProducer eventProducer;

    private final AtomConstructor atomConstructor;
    private final AtomLayer atomLayerLogger;
    private final AtomLayer atomLayerListeners;

    private boolean isOpen;

    TIMInMem() {
	this("TIModel");
    }

    TIMInMem(String name) {
	this.name = Objects.requireNonNull(name);

	tables = new HashMap<>();
	resolver = new AtomResolverOnModel(this);

	logger = new ModelLoggerDefault();
	eventProducer = EventCenter.getInstance().newProducer(name);

	POJOAtomConstructor ac = new POJOAtomConstructor();
	ac.setAtomResolver(resolver);
	atomConstructor = ac;
	atomLayerLogger = new AtomLayerLogger(logger);
	atomLayerListeners = new AtomLayerListeners(eventProducer);

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
    public Atom getAtom(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	switch (id.getType()) {
	case Template:
	    return table.templates.get(id);
	case Instance:
	    return table.instances.get(id);
	default:
	    throw new IllegalArgumentException("Unexpected id: " + id);
	}
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
	ID id = ID.newID(tableName, ID.Type.Template);
	Template0 template = newEmptyTemplate(id);
	postNewAtomEvent(template);
	return template;
    }

    @Override
    public Instance0 newInstance(Template template) {
	String tableName = template.getID().getTableName();
	ID id = ID.newID(tableName, ID.Type.Instance);
	Instance0 instance = newEmptyInstance(id);
	instance.setTemplate(template.getID());
	((Template0) template).addInstance(id);
	postNewAtomEvent(instance);
	return instance;
    }

    private Template0 newEmptyTemplate(ID id) {
	Template0 template;

	// POJO level
	template = atomConstructor.newTemplate(id);
	// Logging level
	template = atomLayerLogger.layer(template);
	// Evented level
	template = atomLayerListeners.layer(template);
	// Mem/Cache level (Must be the last)
	template = new InMemTemplate(template);

	return template;
    }

    private Instance0 newEmptyInstance(ID id) {
	Instance0 instance;

	// POJO level
	instance = atomConstructor.newInstance(id);
	// Logging level
	instance = atomLayerLogger.layer(instance);
	// Evented level
	instance = atomLayerListeners.layer(instance);
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
    public boolean contains(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return false;
	switch (id.getType()) {
	case Template:
	    return table.templates.containsKey(id);
	case Instance:
	    return table.instances.containsKey(id);
	default:
	    throw new IllegalArgumentException("Unexpected id: " + id);
	}
    }

    @Override
    public EventCunsumer getEventCunsumer() {
	return EventCenter.getInstance().newCunsumer(name);
    }

    @Override
    public boolean isOpen() {
	return isOpen;
    }

    @Override
    public final void open() {
	if (open0())
	    eventProducer.postEvent(name, new ModelOpenLog());
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
	if (close0())
	    eventProducer.postEvent(name, new ModelCloseLog());
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

    private void postNewAtomEvent(Atom atom) {
	DTOAtom dto = DTOAtom.valueOf(atom);
	AtomChangeLog changeLog = new AtomChangeLog(null, dto);
	eventProducer.postEvent(atom.getID().toString(), changeLog, atom.getVersion());
    }

    private static class Table {

	final Map<ID, Template0> templates;
	final Map<ID, Instance0> instances;

	Table() {
	    templates = new HashMap<>();
	    instances = new HashMap<>();
	}

    }

    private class InMemAtom extends AbstractViewAtom {

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
