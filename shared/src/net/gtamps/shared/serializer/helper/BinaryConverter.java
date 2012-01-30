package net.gtamps.shared.serializer.helper;

import java.nio.charset.Charset;

import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.Translator;
import net.gtamps.shared.serializer.communication.BinaryObjectSerializer.Const;

public class BinaryConverter {

	private static final Charset charset = Charset.forName("UTF-8");

	public static void writeIntToBytes(final int i, final byte[] modifyBytes, final ArrayPointer p) {
		// final byte[] b = new byte[Integer.SIZE / 8];
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			modifyBytes[j + p.pos()] = 0;
			modifyBytes[j + p.pos()] |= ((i >> (8 * (Integer.SIZE / 8 - j - 1)) & 0xff));
		}
		p.inc(Integer.SIZE / 8);
	}

	public static void writeIntToBytes(final int i, final byte[] modifyBytes) {
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			modifyBytes[j] = 0;
			modifyBytes[j] |= ((i >> (8 * (Integer.SIZE / 8 - j - 1)) & 0xff));
		}
	}

	public static int readIntFromBytes(final byte[] b, final ArrayPointer p) {
		try {
			int i = 0;
			for (int j = 0; j < Integer.SIZE / 8; j++) {
				i = i << 8;
				i |= (b[j + p.pos()] & 0xff);
			}
			p.inc(Integer.SIZE / 8);
			return i;
		} catch (final ArrayIndexOutOfBoundsException e) {
			Logger.e("BinaryConverter", "Current ArrayPointer position is :" + p.pos());
			throw e;
		}
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

	public static float readFloatFromBytes(final byte[] b, final ArrayPointer p) {
		return Float.intBitsToFloat(readIntFromBytes(b, p));
	}

	public static void writeFloatToBytes(final float f, final byte[] modifyBytes, final ArrayPointer p) {
		writeIntToBytes(Float.floatToIntBits(f), modifyBytes, p);
	}

	public static void writeLongToBytes(final long l, final byte[] modifyBytes, final ArrayPointer p) {
		for (int j = 0; j < Long.SIZE / 8; j++) {
			modifyBytes[j + p.pos()] = 0;
			modifyBytes[j + p.pos()] |= ((l >> (8 * (Long.SIZE / 8 - j - 1)) & 0xff));
		}
		p.inc(Long.SIZE / 8);
	}

	public static long readLongFromBytes(final byte[] b, final ArrayPointer p) {
		long l = 0;
		for (int j = 0; j < Long.SIZE / 8; j++) {
			l = l << 8;
			l |= (b[j + p.pos()] & 0xff);
		}
		p.inc(Long.SIZE / 8);
		return l;
	}

	public static void writeStringToBytes(final String s, final byte[] modifyBytes, final ArrayPointer p) {
		try {
			final byte[] byteString = s.getBytes();
			writeIntToBytes(s.length(), modifyBytes, p);

//			s.getBytes(0, s.length(), modifyBytes, p.pos());
			for (int i = 0; i < byteString.length; i++) {
				modifyBytes[i + p.pos()] = byteString[i];
			}
			p.inc(s.length());
		} catch (final ArrayIndexOutOfBoundsException e) {
			System.out.println("error serializing string: "+s);
		}
	}	

	public static String readStringFromBytes(final byte[] modifyBytes, final ArrayPointer p) {
		final int length = readIntFromBytes(modifyBytes, p);
		final String s = new String(modifyBytes,0,p.pos());
//		final String s = new String(modifyBytes, 0, p.pos(), length);
		p.inc(length);
		return s;
	}

	public static void writeBooleanToBytes(final Boolean b, final byte[] buf, final ArrayPointer ps) {
		if (b) {
			buf[ps.pos()] = 1;
		} else {
			buf[ps.pos()] = 0;
		}
		ps.inc(1);
	}

	public static boolean readBooleanFromBytes(final byte[] buf, final ArrayPointer p) {
		p.inc(1);
		return (buf[p.pos() - 1] == 1);
	}

	public static byte readByteFromBytes(final byte[] buf, final ArrayPointer p) {
		try {
			p.inc(1);
			return buf[p.pos() - 1];
		} catch (final ArrayIndexOutOfBoundsException e) {
			Logger.e("BinaryConverter", "Current ArrayPointer position is :" + p.pos());
			throw e;
		}
	}

	public static void writeByteToBytes(final Byte b, final byte[] buf, final ArrayPointer ps) {
		buf[ps.pos()] = b;
		ps.inc(1);
	}
	
	
}
