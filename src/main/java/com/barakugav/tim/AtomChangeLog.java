package com.barakugav.tim;

public class AtomChangeLog extends ChangeLog {

    AtomChangeLog(DTOAtom before, DTOAtom after) {
	super(before, after);
    }

    @Override
    public DTOAtom getBefore() {
	return (DTOAtom) super.getBefore();
    }

    @Override
    public DTOAtom getAfter() {
	return (DTOAtom) super.getAfter();
    }

}
