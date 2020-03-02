package com.barakugav.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;

public class DefaultSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
	return serialize0(obj);
    }

    public static byte[] serialize0(Object obj) {
	if (obj == null)
	    return null;
	try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos)) {
	    out.writeObject(obj);
	    out.flush();
	    return bos.toByteArray();
	} catch (IOException e) {
	    throw new UncheckedIOException(e);
	}
    }

}
