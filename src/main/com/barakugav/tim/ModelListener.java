package com.barakugav.tim;

public interface ModelListener extends TIMListener {

    public void modelOpened(ModelEvent e);

    public void modelBeforeClose(ModelEvent e);

    public void modelClosed(ModelEvent e);

}
