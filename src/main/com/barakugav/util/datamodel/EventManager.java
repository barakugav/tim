package com.barakugav.util.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EventManager {

    private final Set<ModelListener> modelListeners;
    private final Map<String, Set<TableListener>> tableListeners;
    private final Map<ID, Set<AtomListener>> atomListeners;

    EventManager() {
	modelListeners = new HashSet<>();
	tableListeners = new HashMap<>();
	atomListeners = new HashMap<>();
    }

    public boolean addModelListener(ModelListener listener) {
	return modelListeners.add(Objects.requireNonNull(listener));
    }

    public boolean addTableListener(String tableName, TableListener listener) {
	Objects.requireNonNull(tableName);
	Objects.requireNonNull(listener);
	return tableListeners.computeIfAbsent(tableName, n -> new HashSet<>()).add(listener);
    }

    public boolean addAtomListener(ID atom, AtomListener listener) {
	Objects.requireNonNull(atom);
	Objects.requireNonNull(listener);
	return atomListeners.computeIfAbsent(atom, a -> new HashSet<>()).add(listener);
    }

    public boolean removeModelListener(ModelListener listener) {
	return modelListeners.remove(listener);
    }

    public boolean removeTableListener(String tableName, TableListener listener) {
	Set<TableListener> l = tableListeners.get(tableName);
	if (l == null || !l.remove(listener))
	    return false;
	if (l.isEmpty())
	    tableListeners.remove(tableName);
	return true;
    }

    public boolean removeAtomListener(ID atom, AtomListener listener) {
	Set<AtomListener> l = atomListeners.get(atom);
	if (l == null || !l.remove(listener))
	    return false;
	if (l.isEmpty())
	    atomListeners.remove(atom);
	return true;
    }

    void fireModelOpened(ModelEvent e) {
	Objects.requireNonNull(e);
	for (ModelListener listener : modelListeners())
	    listener.modelOpened(e);
    }

    void fireModelBeforeClose(ModelEvent e) {
	Objects.requireNonNull(e);
	for (ModelListener listener : modelListeners())
	    listener.modelBeforeClose(e);
    }

    void fireModelClosed(ModelEvent e) {
	Objects.requireNonNull(e);
	for (ModelListener listener : modelListeners())
	    listener.modelClosed(e);
    }

    void fireTemplateCreated(TableEvent e) {
	Objects.requireNonNull(e);
	for (TableListener listener : tableListeners(e.getSource()))
	    listener.templateCreated(e);
    }

    void fireInstanceCreated(TableEvent e) {
	Objects.requireNonNull(e);
	for (TableListener listener : tableListeners(e.getSource()))
	    listener.instanceCreated(e);
    }

    void fireTemplateDeleted(TableEvent e) {
	Objects.requireNonNull(e);
	for (TableListener listener : tableListeners(e.getSource()))
	    listener.templateDeleted(e);
    }

    void fireInstanceDeleted(TableEvent e) {
	Objects.requireNonNull(e);
	for (TableListener listener : tableListeners(e.getSource()))
	    listener.instanceDeleted(e);
    }

    void fireAtomDeleted(AtomEvent e) {
	Objects.requireNonNull(e);
	for (AtomListener listener : atomListeners(e.getSource()))
	    listener.atomDeleted(e);
    }

    void fireAtomBeforeDelete(AtomEvent e) {
	Objects.requireNonNull(e);
	for (AtomListener listener : atomListeners(e.getSource()))
	    listener.atomBeforeDelete(e);
    }

    void fireAtomPropertyChange(AtomEvent e) {
	Objects.requireNonNull(e);
	for (AtomListener listener : atomListeners(e.getSource()))
	    listener.atomPropertyChange(e);
    }

    private Iterable<ModelListener> modelListeners() {
	return clone(modelListeners);
    }

    private Iterable<TableListener> tableListeners(String tableName) {
	return clone(tableListeners.get(tableName));
    }

    private Iterable<AtomListener> atomListeners(ID atom) {
	return clone(atomListeners.get(atom));
    }

    private static <E> Iterable<E> clone(Collection<E> c) {
	if (c == null || c.isEmpty())
	    return Collections.emptyList();
	return new ArrayList<>(c);
    }

}
