package net.gtamps.shared.serializer.helper;

import java.nio.charset.Charset;

public class BinaryConverter {

	private static final Charset charset = Charset.forName("UTF-8");

	public static void intToBytes(final int i, byte[] modifyBytes, ArrayPointer p) {
		// final byte[] b = new byte[Integer.SIZE / 8];
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			modifyBytes[j + p.pos()] = 0;
			modifyBytes[j + p.pos()] |= ((i >> (8 * (Integer.SIZE / 8 - j - 1)) & 0xff));
		}
		p.inc(Integer.SIZE / 8);
	}

	public static int byteToInt(final byte[] b, ArrayPointer p) {
		int i = 0;
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			i = i << 8;
			i |= (b[j + p.pos()] & 0xff);
		}
		p.inc(Integer.SIZE / 8);
		return i;
	}

	public static float byteToFloat(final byte[] b, ArrayPointer p) {
		return Float.intBitsToFloat(byteToInt(b, p));
	}

	public static void floatToBytes(final float f, byte[] modifyBytes, ArrayPointer p) {
		intToBytes(Float.floatToIntBits(f), modifyBytes, p);
	}

	public static void longToBytes(final long l, byte[] modifyBytes, ArrayPointer p) {
		for (int j = 0; j < Long.SIZE / 8; j++) {
			modifyBytes[j + p.pos()] = 0;
			modifyBytes[j + p.pos()] |= ((l >> (8 * (Long.SIZE / 8 - j - 1)) & 0xff));
		}
		p.inc(Long.SIZE / 8);
	}

	public static long byteTolong(byte[] b, ArrayPointer p) {
		long l = 0;
		for (int j = 0; j < Long.SIZE / 8; j++) {
			l = l << 8;
			l |= (b[j + p.pos()] & 0xff);
		}
		p.inc(Long.SIZE / 8);
		return l;
	}

	public static void stringToByte(final String s, byte[] modifyBytes, ArrayPointer p) {
		byte[] byteString = s.getBytes();
		intToBytes(byteString.length, modifyBytes, p);

		for (int i = 0; i < byteString.length; i++) {
			modifyBytes[i + p.pos()] = byteString[i];
		}
		p.inc(byteString.length);
	}

	public static String byteToString(byte[] modifyBytes, ArrayPointer p) {
		int length = byteToInt(modifyBytes, p);
		String s = new String(modifyBytes, p.pos(), length);
		p.inc(length);
		return s;
	}

	public static void booleanToByte(Boolean b, byte[] buf, ArrayPointer ps) {
		if(b){
			buf[ps.pos()] = 1; 
		} else {
			buf[ps.pos()] = 0;
		}
		ps.inc(1);
	}
	public static boolean byteToBoolean(byte[] buf, ArrayPointer p){
		p.inc(1);
		return (buf[p.pos()-1] == 1); 
	}
}
