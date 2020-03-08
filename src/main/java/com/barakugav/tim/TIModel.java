package com.barakugav.tim;

import java.util.Collection;
import java.util.function.Predicate;

public interface TIModel {

    Collection<String> tableNames();

    Collection<Template> getTemplates(String tableName);

    Collection<Template> getTemplates(String tableName, Predicate<Template> condition);

    Collection<Instance> getInstances(String tableName);

    Collection<Instance> getInstances(String tableName, Predicate<Instance> condition);

    Atom getAtom(ID id);

    Template getTemplate(ID id);

    Instance getIntance(ID id);

    Template newTemplate(String tableName);

    Instance newInstance(Template template);

    boolean contains(ID id);

    String getEmagnetarTopic();

    boolean isOpen();

    void open();

    void close();

    public static TIModel getDefault() {
	return new TIMXML();
    }

}
