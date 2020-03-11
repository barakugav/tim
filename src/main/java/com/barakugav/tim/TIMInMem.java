package com.barakugav.tim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.barakugav.emagnetar.EMagnetar;
import com.barakugav.emagnetar.Producer;
import com.barakugav.tim.dto.DTOAtom;
import com.barakugav.tim.log.ModelLogger;

abstract class TIMInMem implements TIModel {

    private final String name;

    private final Map<String, Table> tables;
    private final AtomResolver resolver;

    private final ModelLogger logger;
    private final Producer eventProducer;

    private final AtomConstructor atomConstructor;
    private final AtomLayerLogger atomLayerLogger;
    private final AtomLayerEvents atomLayerEvents;

    private boolean isOpen;

    TIMInMem() {
	this("TIModel");
    }

    TIMInMem(String name) {
	this.name = Objects.requireNonNull(name);

	tables = new HashMap<>();
	resolver = new AtomResolverOnModel(this);

	logger = ModelLogger.getDefault();
	eventProducer = EMagnetar.newProducer(this.name);

	atomConstructor = new POJOAtomConstructor();
	atomConstructor.setAtomResolver(resolver);
	atomLayerLogger = new AtomLayerLogger(logger);
	atomLayerEvents = new AtomLayerEvents(eventProducer);

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
	return Collections.unmodifiableCollection(table.getTemplates());
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
	return Collections.unmodifiableCollection(table.getInstances());
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
	return table.getAtom(id);
    }

    @Override
    public Template0 getTemplate(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	return table.getTemplate(id);
    }

    @Override
    public Instance0 getIntance(ID id) {
	Table table = tables.get(id.getTableName());
	if (table == null)
	    return null;
	return table.getInstance(id);
    }

    @Override
    public Template0 newTemplate(String tableName) {
	ID id = ID.newID(tableName);
	Template0 template = newEmptyTemplate(id);
	postNewAtomEvent(template);
	return template;
    }

    @Override
    public Instance0 newInstance(Template template) {
	String tableName = template.getID().getTableName();
	ID id = ID.newID(tableName);
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
	template = atomLayerEvents.layer(template);
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
	instance = atomLayerEvents.layer(instance);
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
	return table != null && table.contains(id);
    }

    @Override
    public String getEmagnetarTopic() {
	return eventProducer.getTopic();
    }

    @Override
    public boolean isOpen() {
	return isOpen;
    }

    @Override
    public final void open() {
	if (isOpen())
	    return;
	beforeOpen0();
	open0();
	afterOpen0();
	isOpen = true;
    }

    protected void beforeOpen0() {
	atomLayerLogger.disable();
	atomLayerEvents.disable();
	atomConstructor.setAtomResolver(new AtomResolver() {

	    @Override
	    public Template0 getTemplate(ID id) {
		return getOrCreateEmptyTemplate(id);
	    }

	    @Override
	    public Instance0 getInstance(ID id) {
		return getOrCreateEmptyInstance(id);
	    }

	    @Override
	    public Atom apply(ID id) {
		return getAtom(id);
	    }
	});
    }

    protected void open0() {
	// Nothing to do here, should be overrided be sub implementation
    }

    protected void afterOpen0() {
	atomLayerLogger.enable();
	atomLayerEvents.enable();
	atomConstructor.setAtomResolver(resolver);
    }

    @Override
    public void close() {
	if (!isOpen())
	    return;

	for (Table table : tables.values()) {
	    table.templates.clear();
	    table.instances.clear();
	}
	tables.clear();

	isOpen = false;
    }

    private void addToMem(Atom0 atom) {
	ID id = atom.getID();
	String tableName = id.getTableName();
	Table table = tables.computeIfAbsent(tableName, n -> new Table());
	table.add(atom);
    }

    private void removeFromMem(Atom atom) {
	ID id = atom.getID();
	String tableName = id.getTableName();
	Table table = tables.get(tableName);
	if (table != null)
	    table.remove(id);
    }

    private void postNewAtomEvent(Atom atom) {
	String key = atom.getID().toString();
	Object data = DTOAtom.valueOf(atom);
	eventProducer.postEvent(key, data);
    }

    private static class Table {

	private final Map<ID, Atom0> atoms;
	private final Set<ID> templates;
	private final Set<ID> instances;

	Table() {
	    atoms = new HashMap<>();
	    templates = new HashSet<>();
	    instances = new HashSet<>();
	}

	@Override
	public String toString() {
	    return "atoms: " + atoms.values();
	}

	Atom0 getAtom(ID id) {
	    return atoms.get(id);
	}

	Template0 getTemplate(ID id) {
	    return (Template0) atoms.get(id);
	}

	Instance0 getInstance(ID id) {
	    return (Instance0) atoms.get(id);
	}

	List<Template0> getTemplates() {
	    List<Template0> result = new ArrayList<>(templates.size());
	    for (ID template : templates)
		result.add(getTemplate(template));
	    return result;
	}

	List<Instance0> getInstances() {
	    List<Instance0> result = new ArrayList<>(instances.size());
	    for (ID instance : instances)
		result.add(getInstance(instance));
	    return result;
	}

	boolean contains(ID id) {
	    return atoms.containsKey(id);
	}

	void remove(ID id) {
	    Atom0 atom = atoms.remove(id);
	    if (atom instanceof Template)
		templates.remove(id);
	    else
		instances.remove(id);
	}

	void add(Atom0 atom) {
	    ID id = atom.getID();
	    if (atoms.put(id, atom) != null)
		throw new IllegalArgumentException("Atom with same ID already exists: " + id);
	    if (atom instanceof Template)
		templates.add(id);
	    else
		instances.add(id);
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
