package com.barakugav.tim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class POJOAtomConstructor implements AtomConstructor {

    private AtomResolver resolver;

    POJOAtomConstructor() {
    }

    @Override
    public void setAtomResolver(AtomResolver resolver) {
	this.resolver = Objects.requireNonNull(resolver);
    }

    @Override
    public Template0 newTemplate(ID id) {
	return newTemplate0(id);
    }

    @Override
    public Instance0 newInstance(ID id) {
	return newInstance0(id);
    }

    Template0 newTemplate0(ID id) {
	return new POJOTemplate(id);
    }

    Instance0 newInstance0(ID id) {
	return new POJOInstance(id);
    }

    private class POJOAtom extends AbstractAtom implements Atom0 {

	private final ID id;
	private final Map<String, Object> properties;
	private boolean isAlive;
	long version;

	POJOAtom(ID id) {
	    this.id = Objects.requireNonNull(id);
	    this.properties = new HashMap<>();
	    isAlive = true;
	}

	@Override
	public ID getID() {
	    return id;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> V getProperty(String key) {
	    return (V) properties.get(key);
	}

	@Override
	public boolean containsProperty(String key) {
	    return properties.containsKey(key);
	}

	@Override
	public Map<String, Object> getProperties() {
	    return new HashMap<>(properties);
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    if (!Atom.isValidPropertyKey(key) || Objects.equals(value, properties.put(key, value)))
		return false;
	    version++;
	    return true;
	}

	@Override
	public boolean setProperties(Map<String, ? extends Object> properties) {
	    if (properties == null)
		properties = Collections.emptyMap();
	    Map<String, Object> newProperties = new HashMap<>(properties);
	    newProperties.keySet().removeIf(k -> !Atom.isValidPropertyKey(k));

	    if (Objects.equals(newProperties, this.properties))
		return false;
	    this.properties.clear();
	    this.properties.putAll(newProperties);
	    version++;
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    if (!properties.containsKey(key))
		return false;
	    properties.remove(key);
	    version++;
	    return true;
	}

	@Override
	public boolean delete() {
	    if (!isAlive)
		return false;
	    isAlive = false;
	    version++;
	    return true;
	}

	@Override
	public long getVersion() {
	    return version;
	}

	@Override
	public boolean isAlive() {
	    return isAlive;
	}

	@Override
	public String toString() {
	    return "Atom[" + id + "]";
	}

    }

    private class POJOTemplate extends POJOAtom implements Template0 {

	private final Set<ID> instances;

	POJOTemplate(ID id) {
	    super(id);
	    instances = new HashSet<>(0);
	}

	@Override
	public boolean delete() {
	    if (!super.delete())
		return false;
	    for (ID instance : instances)
		resolver.getInstance(instance).delete();
	    instances.clear();
	    return true;
	}

	@Override
	public Collection<Instance> getInstances() {
	    Collection<Instance> result = new ArrayList<>(instances.size());
	    for (ID instance : instances)
		result.add(resolver.getInstance(instance));
	    return result;
	}

	@Override
	public Collection<ID> getInstances0() {
	    return Collections.unmodifiableSet(instances);
	}

	@Override
	public int getInstanceCount() {
	    return instances.size();
	}

	@Override
	public boolean addInstance(ID instance) {
	    assert getID().equals(resolver.getInstance(instance).getTemplate0());
	    if (!instances.add(instance))
		return false;
	    version++;
	    return true;
	}

	@Override
	public boolean removeInstance(ID instance) {
	    if (!instances.remove(instance))
		return false;
	    version++;
	    return true;
	}

	@Override
	public boolean setInstances(Collection<ID> instances) {
	    if (instances == null)
		instances = Collections.emptyList();
	    if (instances.equals(this.instances))
		return false;
	    this.instances.clear();
	    this.instances.addAll(instances);
	    version++;
	    return true;
	}

    }

    private class POJOInstance extends POJOAtom implements Instance0 {

	private ID template;

	POJOInstance(ID id) {
	    super(id);
	}

	@Override
	public <V> V getProperty(String key) {
	    return super.containsProperty(key) ? super.getProperty(key) : getTemplate().getProperty(key);
	}

	@Override
	public boolean containsProperty(String key) {
	    return super.containsProperty(key) || getTemplate().containsProperty(key);
	}

	@Override
	public Map<String, Object> getProperties() {
	    Map<String, Object> result = getTemplate().getProperties();
	    result.putAll(super.getProperties());
	    return result;
	}

	@Override
	public Template0 getTemplate() {
	    return resolver.getTemplate(template);
	}

	@Override
	public ID getTemplate0() {
	    return template;
	}

	@Override
	public boolean setTemplate(ID template) {
	    if (Objects.equals(template, this.template))
		return false;
	    this.template = template;
	    version++;
	    return true;
	}

	@Override
	public boolean delete() {
	    if (!super.delete())
		return false;
	    getTemplate().removeInstance(getID());
	    return true;
	}

    }

}
