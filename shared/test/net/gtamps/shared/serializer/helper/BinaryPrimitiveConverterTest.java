package net.gtamps.shared.serializer.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryPrimitiveConverterTest {

	@Test
	public void testFloatToBytes() {
		for (int input = -10000; input < 10000; input += 1344) {
			float floatinput = (float)(input+0.234234);
//			System.out.println("input "+floatinput);
			byte[] b = BinaryPrimitiveConverter.floatToBytes(floatinput);
//			 for(int j=0; j<b.length; j++){
//			 System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
//			 System.out.println("b"+j+": "+b[j]);
//			 }
			 
//			 System.out.println("end: "+BinaryPrimitiveConverter.byteToFloat(b));
			
			assertEquals(floatinput, BinaryPrimitiveConverter.byteToFloat(b),0.0f);
		}
	}

	@Test
	public void testIntToBytes() {
		for (int input = -10000; input < 10000; input += 1344) {
			// System.out.println("input "+input);
			byte[] b = BinaryPrimitiveConverter.intToBytes(input);
			// for(int j=0; j<b.length; j++){
			// System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
			// System.out.println("b"+j+": "+b[j]);
			// }
			// System.out.println("end: "+Integer.toBinaryString(BinaryPrimitiveConverter.byteToInt(b)));
			// System.out.println("end: "+BinaryPrimitiveConverter.byteToInt(b));
			assertEquals(input, BinaryPrimitiveConverter.byteToInt(b));
		}
	}

	@Test
	public void testLongToBytes() {
		for (int input = -10000; input < 10000; input += 1344) {
			// System.out.println("input "+input);
			byte[] b = BinaryPrimitiveConverter.longToBytes(input);
			// for(int j=0; j<b.length; j++){
			// System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
			// System.out.println("b"+j+": "+b[j]);
			// }
			// System.out.println("end: "+Integer.toBinaryString(BinaryPrimitiveConverter.byteToInt(b)));
			// System.out.println("end: "+BinaryPrimitiveConverter.byteToInt(b));
			assertEquals(input, BinaryPrimitiveConverter.byteTolong(b));
		}
	}
	
	@Test
	public void testCharArrayToBytes() {
		char[] c = {'a','b','c','æ','?','ß'};
		byte[] b = BinaryPrimitiveConverter.charArrayToByte(c);
//		for (int j = 0; j < b.length; j++) {
//			System.out.println("b" + j + ": " + Integer.toBinaryString(b[j]));
//			System.out.println("b" + j + ": " + b[j]);
//		}
//		System.out.println(BinaryPrimitiveConverter.byteToCharArray(b));
		assertArrayEquals(c, BinaryPrimitiveConverter.byteToCharArray(b));
	}

}
