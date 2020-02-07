package com.barakugav.util.tim;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class AtomConstructorPOJO implements AtomConstructor {

    AtomConstructorPOJO() {
    }

    @Override
    public Template0 newTemplate(ID id) {
	return new POJOTemplate(id);
    }

    @Override
    public Instance0 newInstance(ID id) {
	return new POJOInstance(id);
    }

    private static class POJOAtom implements Atom0 {

	private final ID id;
	private final Map<String, Object> properties;
	private boolean isAlive;

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
	public Map<String, Object> getProperties(boolean includeHidden) {
	    Map<String, Object> result = new HashMap<>();
	    if (!includeHidden)
		result.keySet().removeIf(Atom::isHiddenProperty);
	    return result;
	}

	@Override
	public <V> boolean setProperty(String key, V value) {
	    return Atom.isValidPropertyKey(key) && !Objects.equals(value, properties.put(key, value));
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
	    return true;
	}

	@Override
	public boolean removeProperty(String key) {
	    if (!properties.containsKey(key))
		return false;
	    properties.remove(key);
	    return true;
	}

	@Override
	public boolean delete() {
	    if (!isAlive)
		return false;
	    isAlive = false;
	    return true;
	}

	@Override
	public boolean isAlive() {
	    return isAlive;
	}

	@Override
	public String toString() {
	    return "Atom[" + id + "]";
	}

	@Override
	public boolean equals(Object other) {
	    if (other == this)
		return true;
	    if (!(other instanceof Atom))
		return false;

	    Atom o = (Atom) other;
	    return o.getID().equals(id);
	}

	@Override
	public int hashCode() {
	    return id.hashCode();
	}

    }

    private static class POJOTemplate extends POJOAtom implements Template0 {

	private final Set<Instance0> instances;

	POJOTemplate(ID id) {
	    super(id);
	    instances = new HashSet<>(0);
	}

	@Override
	public boolean delete() {
	    if (!super.delete())
		return false;
	    for (Instance instance : instances)
		instance.delete();
	    instances.clear();
	    return true;
	}

	@Override
	public Collection<Instance> getInstances() {
	    return Collections.unmodifiableCollection(instances);
	}

	@Override
	public Collection<Instance0> getInstances0() {
	    return instances;
	}

	@Override
	public int getInstanceCount() {
	    return instances.size();
	}

	@Override
	public boolean addInstance(Instance0 instance) {
	    assert equals(instance.getTemplate());
	    return instances.add(instance);
	}

	@Override
	public boolean setInstances(Collection<Instance0> instances) {
	    if (instances == null)
		instances = Collections.emptyList();
	    if (instances.equals(this.instances))
		return false;
	    this.instances.clear();
	    this.instances.addAll(instances);
	    return true;
	}

    }

    private static class POJOInstance extends POJOAtom implements Instance0 {

	private Template0 template;

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
	public Map<String, Object> getProperties(boolean includeHidden) {
	    Map<String, Object> result = getTemplate().getProperties(includeHidden);
	    result.putAll(super.getProperties(includeHidden));
	    return result;
	}

	@Override
	public Template0 getTemplate() {
	    return template;
	}

	@Override
	public boolean setTemplate(Template0 template) {
	    if (Objects.equals(template, this.template))
		return false;
	    this.template = template;
	    return true;
	}

    }

}
