package com.barakugav.util.tim;

public interface AtomListener {

    public void atomDeleted(AtomEvent e);

    public void atomBeforeDelete(AtomEvent e);

    public void atomPropertyChange(AtomEvent e);

}
