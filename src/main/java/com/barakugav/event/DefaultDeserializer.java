package com.barakugav.event;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.UncheckedIOException;

public class DefaultDeserializer implements Deserializer {

    @Override
    public Object deserialize(byte[] bytes) throws ClassNotFoundException {
	return deserialize0(bytes);
    }

    public static Object deserialize0(byte[] bytes) throws ClassNotFoundException {
	if (bytes == null)
	    return null;
	try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
	    return in.readObject();
	} catch (IOException e) {
	    throw new UncheckedIOException(e);
	}
    }

}
