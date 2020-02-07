package com.barakugav.util.tim;

import java.util.Iterator;

interface ModelLogger {

    void log(ModelLog log);

    Iterator<ModelLog> getLogs(long begin, long end);

}
