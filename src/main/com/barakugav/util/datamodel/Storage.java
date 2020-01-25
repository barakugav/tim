package com.barakugav.util.datamodel;

import java.util.Collection;

interface Storage {

    Collection<String> tableNames();

    Collection<Template> getTemplates(String tableName);

    Collection<Instance> getInstances(String tableName);

    Template getTemplate(ID id);

    Instance getIntance(ID id);

    Template newTemplate(String tableName);

    Instance newInstance(Template template);

    void open();

    void close();

}
