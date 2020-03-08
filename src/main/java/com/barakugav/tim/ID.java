package com.barakugav.tim;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public final class ID implements Serializable {

    public static enum Type {
	Template, Instance
    }

    private final String tableName;
    private final Type type;
    private final byte[] data;
    private transient int hash;

    private static final Random rand = new Random();
    private static final int DATA_LENGTH = 16;
    private static final String SEPARATOR = ".";

    private ID(String tableName, Type type, byte[] data) {
	this.tableName = Objects.requireNonNull(tableName);
	this.type = Objects.requireNonNull(type);
	this.data = data.clone();
	computeHashCode();
    }

    public String getTableName() {
	return tableName;
    }

    public Type getType() {
	return type;
    }

    @Override
    public boolean equals(Object other) {
	if (other == this)
	    return true;
	if (!(other instanceof ID))
	    return false;

	ID o = (ID) other;
	return o.hash == hash && Arrays.equals(o.data, data) && Objects.equals(o.tableName, tableName);
    }

    @Override
    public int hashCode() {
	return hash;
    }

    @Override
    public String toString() {
	return tableName + SEPARATOR + type.name() + SEPARATOR + bytesToHexString(data);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	in.defaultReadObject();
	computeHashCode();
    }

    private void computeHashCode() {
	hash = Arrays.hashCode(data) ^ Objects.hashCode(tableName) ^ type.hashCode();
    }

    public static ID newID(String tableName, Type type) {
	byte[] b = new byte[DATA_LENGTH];
	rand.nextBytes(b);
	return new ID(tableName, type, b);
    }

    public static ID valueOf(String s) throws ParseException {
	try {
	    String[] st = s.split("\\" + SEPARATOR);
	    if (st.length != 3)
		throw new ParseException(s, 0);
	    String tableName = st[0];
	    String typeStr = st[1];
	    Type type = Type.valueOf(typeStr);
	    String dataStr = st[2];
	    byte[] data = hexStringToBytes(dataStr);
	    return new ID(tableName, type, data);
	} catch (IllegalArgumentException e) {
	    throw new ParseException(e.getMessage(), 0);
	}
    }

    private static String bytesToHexString(byte[] bytes) {
	char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	char[] hexChars = new char[bytes.length * 2];
	for (int j = 0; j < bytes.length; j++) {
	    int v = bytes[j] & 0xFF;
	    hexChars[j * 2] = hexArray[v >>> 4];
	    hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	}
	return new String(hexChars);
    }

    private static byte[] hexStringToBytes(String s) throws ParseException {
	int len = s.length();
	if (len % 2 != 0)
	    throw new ParseException("invalid length", 0);
	byte[] bytes = new byte[len / 2];
	for (int i = 0; i < len; i += 2)
	    bytes[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
	return bytes;
    }

}
