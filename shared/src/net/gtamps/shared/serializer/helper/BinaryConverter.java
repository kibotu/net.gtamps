package net.gtamps.shared.serializer.helper;

import java.nio.charset.Charset;

public class BinaryConverter {

	private static final Charset charset = Charset.forName("UTF-8");

	public static void writeIntToBytes(final int i, byte[] modifyBytes, ArrayPointer p) {
		// final byte[] b = new byte[Integer.SIZE / 8];
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			modifyBytes[j + p.pos()] = 0;
			modifyBytes[j + p.pos()] |= ((i >> (8 * (Integer.SIZE / 8 - j - 1)) & 0xff));
		}
		p.inc(Integer.SIZE / 8);
	}
	
	public static void writeIntToBytes(final int i, byte[] modifyBytes) {
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			modifyBytes[j] = 0;
			modifyBytes[j] |= ((i >> (8 * (Integer.SIZE / 8 - j - 1)) & 0xff));
		}
	}

	public static int readIntFromBytes(final byte[] b, ArrayPointer p) {
		int i = 0;
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			i = i << 8;
			i |= (b[j + p.pos()] & 0xff);
		}
		p.inc(Integer.SIZE / 8);
		return i;
	}
	/*
	 * reads first int in bytearray
	 */
	public static int readIntFromBytes(final byte[] b) {
		int i = 0;
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			i = i << 8;
			i |= (b[j] & 0xff);
		}
		return i;
	}

	public static float readFloatFromBytes(final byte[] b, ArrayPointer p) {
		return Float.intBitsToFloat(readIntFromBytes(b, p));
	}

	public static void writeFloatToBytes(final float f, byte[] modifyBytes, ArrayPointer p) {
		writeIntToBytes(Float.floatToIntBits(f), modifyBytes, p);
	}

	public static void writeLongToBytes(final long l, byte[] modifyBytes, ArrayPointer p) {
		for (int j = 0; j < Long.SIZE / 8; j++) {
			modifyBytes[j + p.pos()] = 0;
			modifyBytes[j + p.pos()] |= ((l >> (8 * (Long.SIZE / 8 - j - 1)) & 0xff));
		}
		p.inc(Long.SIZE / 8);
	}

	public static long readLongFromBytes(byte[] b, ArrayPointer p) {
		long l = 0;
		for (int j = 0; j < Long.SIZE / 8; j++) {
			l = l << 8;
			l |= (b[j + p.pos()] & 0xff);
		}
		p.inc(Long.SIZE / 8);
		return l;
	}

	public static void writeStringToBytes(final String s, byte[] modifyBytes, ArrayPointer p) {
		byte[] byteString = s.getBytes();
		writeIntToBytes(byteString.length, modifyBytes, p);

		for (int i = 0; i < byteString.length; i++) {
			modifyBytes[i + p.pos()] = byteString[i];
		}
		p.inc(byteString.length);
	}

	public static String readStringFromBytes(byte[] modifyBytes, ArrayPointer p) {
		int length = readIntFromBytes(modifyBytes, p);
		String s = new String(modifyBytes, p.pos(), length);
		p.inc(length);
		return s;
	}

	public static void writeBooleanToBytes(Boolean b, byte[] buf, ArrayPointer ps) {
		if(b){
			buf[ps.pos()] = 1; 
		} else {
			buf[ps.pos()] = 0;
		}
		ps.inc(1);
	}
	public static boolean readBooleanFromBytes(byte[] buf, ArrayPointer p){
		p.inc(1);
		return (buf[p.pos()-1] == 1); 
	}
	public static byte readByteFromBytes(byte[] buf, ArrayPointer p){
		p.inc(1);
		return buf[p.pos()-1];
	}
	
	public static void writeByteToBytes(Byte b, byte[] buf, ArrayPointer ps) {
		buf[ps.pos()] = b; 
		ps.inc(1);
	}
}
