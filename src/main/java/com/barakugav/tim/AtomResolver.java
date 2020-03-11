package com.barakugav.tim;

import java.util.function.Function;

interface AtomResolver extends Function<ID, Atom> {

    Instance0 getInstance(ID id);

    Template0 getTemplate(ID id);

}
