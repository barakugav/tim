package com.barakugav.util.datamodel;

public interface ModelListener {

    public void modelOpened(ModelEvent e);

    public void modelBeforeClose(ModelEvent e);

    public void modelClosed(ModelEvent e);

}
