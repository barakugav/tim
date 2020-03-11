package com.barakugav.tim;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public final class ID implements Serializable {

    private final String tableName;
    private final byte[] data;
    private transient int hash;

    private static final Random rand = new Random();
    private static final int DATA_LENGTH = 16;
    private static final String SEPARATOR = ".";

    private ID(String tableName, byte[] data) {
	this.tableName = Objects.requireNonNull(tableName);
	this.data = data.clone();
	computeHashCode();
    }

    public String getTableName() {
	return tableName;
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
	return tableName + SEPARATOR + bytesToHexString(data);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	in.defaultReadObject();
	computeHashCode();
    }

    private void computeHashCode() {
	hash = Arrays.hashCode(data) ^ Objects.hashCode(tableName);
    }

    public static ID newID(String tableName) {
	byte[] b = new byte[DATA_LENGTH];
	rand.nextBytes(b);
	return new ID(tableName, b);
    }

    public static ID valueOf(String s) {
	String[] st = s.split("\\" + SEPARATOR);
	if (st.length != 2)
	    throw new IllegalArgumentException(s);
	String tableName = st[0];
	String dataStr = st[1];
	byte[] data = hexStringToBytes(dataStr);
	return new ID(tableName, data);
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

    private static byte[] hexStringToBytes(String s) {
	int len = s.length();
	if (len % 2 != 0)
	    throw new IllegalArgumentException("invalid length");
	byte[] bytes = new byte[len / 2];
	for (int i = 0; i < len; i += 2)
	    bytes[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
	return bytes;
    }

}
