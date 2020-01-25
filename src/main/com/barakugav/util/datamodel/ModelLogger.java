package com.barakugav.util.datamodel;

import java.util.Iterator;

interface ModelLogger {

    void log(ModelLog log);

    Iterator<ModelLog> getLogs(long begin, long end);

}
