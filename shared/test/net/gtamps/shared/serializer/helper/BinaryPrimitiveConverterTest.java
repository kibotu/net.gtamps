package net.gtamps.shared.serializer.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryPrimitiveConverterTest {

	@Test
	public void testFloatToBytes() {
		byte[] b = new byte[2048];
		ArrayPointer inputpointer = new ArrayPointer();
		ArrayPointer outputpointer = new ArrayPointer();
		for (int input = -10000; input < 10000; input += 1344) {
			float floatinput = (float)(input+0.234234);
//			System.out.println("input "+floatinput);
			BinaryPrimitiveConverter.floatToBytes(floatinput, b, inputpointer);
//			 for(int j=0; j<b.length; j++){
//			 System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
//			 System.out.println("b"+j+": "+b[j]);
//			 }
			 
//			 System.out.println("end: "+BinaryPrimitiveConverter.byteToFloat(b));
			
			assertEquals(floatinput, BinaryPrimitiveConverter.byteToFloat(b, outputpointer),0.0f);
		}
	}

	@Test
	public void testIntToBytes() {
		byte[] b = new byte[2048];
		ArrayPointer inputpointer = new ArrayPointer();
		ArrayPointer outputpointer = new ArrayPointer();
		for (int input = -10000; input < 10000; input += 1344) {
			// System.out.println("input "+input);
			BinaryPrimitiveConverter.intToBytes(input, b, inputpointer);
			// for(int j=0; j<b.length; j++){
			// System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
			// System.out.println("b"+j+": "+b[j]);
			// }
			// System.out.println("end: "+Integer.toBinaryString(BinaryPrimitiveConverter.byteToInt(b)));
			// System.out.println("end: "+BinaryPrimitiveConverter.byteToInt(b));
			assertEquals(input, BinaryPrimitiveConverter.byteToInt(b, outputpointer));
		}
	}

	@Test
	public void testLongToBytes() {
		byte[] b = new byte[2048];
		ArrayPointer inputpointer = new ArrayPointer();
		ArrayPointer outputpointer = new ArrayPointer();
		for (int input = -10000; input < 10000; input += 1344) {
			// System.out.println("input "+input);
			BinaryPrimitiveConverter.longToBytes(input, b, inputpointer);
			// for(int j=0; j<b.length; j++){
			// System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
			// System.out.println("b"+j+": "+b[j]);
			// }
			// System.out.println("end: "+Integer.toBinaryString(BinaryPrimitiveConverter.byteToInt(b)));
			// System.out.println("end: "+BinaryPrimitiveConverter.byteToInt(b));
			assertEquals(input, BinaryPrimitiveConverter.byteTolong(b, outputpointer));
		}
	}
	
	@Test
	public void testCharArrayToBytes() {
		String s = "eabcæ?ß";
		ArrayPointer inputpointer = new ArrayPointer();
		ArrayPointer outputpointer = new ArrayPointer();
		byte[] b = new byte[2048];
		BinaryPrimitiveConverter.stringToByte(s, b, inputpointer);
//		for (int j = 0; j < b.length; j++) {
//			System.out.println("b" + j + ": " + Integer.toBinaryString(b[j]));
//			System.out.println("b" + j + ": " + b[j]);
//		}
//		System.out.println(BinaryPrimitiveConverter.byteToCharArray(b));
		assertEquals(s, BinaryPrimitiveConverter.byteToString(b, outputpointer));
	}

}
