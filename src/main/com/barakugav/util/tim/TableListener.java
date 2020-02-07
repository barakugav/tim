package com.barakugav.util.tim;

public interface TableListener {

    public void templateCreated(TableEvent e);

    public void instanceCreated(TableEvent e);

    public void templateDeleted(TableEvent e);

    public void instanceDeleted(TableEvent e);

}
