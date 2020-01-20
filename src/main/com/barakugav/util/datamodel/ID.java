package com.barakugav.util.datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Random;

public final class ID {

    private final byte[] data;
    private transient int hash;

    public static final ID NULLID;

    private static final Random rand = new Random();
    private static final int DATA_LENGTH = 16;

    static {
	byte[] nullIDValue = new byte[DATA_LENGTH];
	Arrays.fill(nullIDValue, (byte) 0);
	NULLID = new ID(nullIDValue);
    }

    private ID(byte[] data) {
	this.data = data.clone();
	hash = Arrays.hashCode(this.data);
    }

    @Override
    public boolean equals(Object other) {
	if (other == this)
	    return true;
	if (!(other instanceof ID))
	    return false;

	ID o = (ID) other;
	return o.hash == hash && Arrays.equals(o.data, data);
    }

    @Override
    public int hashCode() {
	return hash;
    }

    @Override
    public String toString() {
	return bytesToHexString(data);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	in.defaultReadObject();
	hash = Arrays.hashCode(data);
    }

    public static ID newID() {
	byte[] b = new byte[DATA_LENGTH];
	rand.nextBytes(b);
	return new ID(b);
    }

    public static ID valueOf(String s) throws ParseException {
	return valueOf(hexStringToBytes(s));
    }

    public static ID valueOf(byte[] bytes) {
	if (bytes.length != DATA_LENGTH)
	    throw new IllegalArgumentException("invalid bytes length");
	return new ID(bytes.clone());
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
