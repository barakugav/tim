package com.barakugav.tim;

public interface AtomListener extends TIMListener {

    public void atomDeleted(AtomEvent e);

    public void atomBeforeDelete(AtomEvent e);

    public void atomPropertyChange(AtomEvent e);

}
