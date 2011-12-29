package net.gtamps.shared.serializer.helper;

public class BinaryPrimitiveConverter {

	public static byte[] intToBytes(final int i) {
		final byte[] b = new byte[Integer.SIZE / 8];
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			b[j] = 0;
			b[j] |= ((i >> (8 * (Integer.SIZE / 8 - j - 1)) & 0xff));
		}
		return b;
	}

	public static int byteToInt(final byte[] b) {
		int i = 0;
		for (int j = 0; j < Integer.SIZE / 8; j++) {
			i = i << 8;
			i |= (b[j] & 0xff);
		}
		return i;
	}

	public static float byteToFloat(final byte[] b) {
		return Float.intBitsToFloat(byteToInt(b));
	}

	public static byte[] floatToBytes(final float f) {
		return intToBytes(Float.floatToIntBits(f));
	}

	public static byte[] longToBytes(final long l) {
		final byte[] b = new byte[Long.SIZE / 8];
		for (int j = 0; j < Long.SIZE / 8; j++) {
			b[j] = 0;
			b[j] |= ((l >> (8 * (Long.SIZE / 8 - j - 1)) & 0xff));
		}
		return b;
	}

	public static long byteTolong(final byte[] b) {
		long l = 0;
		for (int j = 0; j < Long.SIZE / 8; j++) {
			l = l << 8;
			l |= (b[j] & 0xff);
		}
		return l;
	}

	public static byte[] charArrayToByte(final char[] c) {
		byte[] b = new byte[c.length*Character.SIZE/8];
		for (int i = 0; i < c.length; i++) {
			b[i*2] = (byte) (c[i] & 0xff);
			b[i*2+1] = (byte) ((c[i] & 0xff00) >> 8);
		}
		return b;
	}
	public static char[] byteToCharArray(final byte[] b){
		char[] c = new char[b.length/2];
		for(int i=0; i<c.length; i++){
			c[i] |= (char) (b[i*2+1]);
			c[i] =  (char) (c[i]<<8);
			c[i] |= (char) (b[i*2] & 0xff);
		}
		return c;
	}
}
