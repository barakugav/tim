package com.barakugav.tim.log;

import java.util.Iterator;

public interface ModelLogger {

    public void log(ModelLog log);

    public Iterator<ModelLog> getLogs(long begin, long end);

    public static ModelLogger getDefault() {
	return new DefaultModelLogger();
    }

}
