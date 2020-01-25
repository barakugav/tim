package com.barakugav.util.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

class ModelLoggerDefault implements ModelLogger {

    private final SortedMap<Long, Collection<ModelLog>> logs;

    ModelLoggerDefault() {
	logs = new TreeMap<>();
    }

    @Override
    public void log(ModelLog log) {
	Objects.requireNonNull(log);
	Long key = Long.valueOf(log.getTimePoint());
	logs.computeIfAbsent(key, k -> new ArrayList<>()).add(log);
    }

    @Override
    public Iterator<ModelLog> getLogs(long begin, long end) {
	return new LogsIterator(begin, end);
    }

    private class LogsIterator implements Iterator<ModelLog> {

	private final Iterator<Collection<ModelLog>> macroIt;
	private Iterator<ModelLog> microIt;

	LogsIterator(long begin, long end) {
	    macroIt = logs.subMap(Long.valueOf(begin), Long.valueOf(end)).values().iterator();
	    microIt = Collections.emptyIterator();
	}

	@Override
	public boolean hasNext() {
	    while (true) {
		if (microIt.hasNext())
		    return true;
		if (!macroIt.hasNext())
		    return false;
		microIt = macroIt.next().iterator();
	    }
	}

	@Override
	public ModelLog next() {
	    if (!hasNext())
		throw new NoSuchElementException();
	    return microIt.next();
	}

    }

}
