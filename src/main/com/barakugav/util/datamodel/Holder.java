package com.barakugav.util.datamodel;

import java.util.Objects;
import java.util.function.Supplier;

class Holder<T> implements Supplier<T> {

    private final T value;

    public Holder(T value) {
	this.value = value;
    }

    @Override
    public T get() {
	return value;
    }

    @Override
    public String toString() {
	return "Holder[" + value + "]";
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object other) {
	if (other == this)
	    return true;
	if (!(other instanceof Holder))
	    return false;

	Holder<?> o = (Holder<?>) other;
	return Objects.equals(o.value, value);
    }

}
