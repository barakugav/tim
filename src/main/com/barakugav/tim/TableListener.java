package com.barakugav.tim;

public interface TableListener extends TIMListener {

    public void templateCreated(TableEvent e);

    public void instanceCreated(TableEvent e);

    public void templateDeleted(TableEvent e);

    public void instanceDeleted(TableEvent e);

}
