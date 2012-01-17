package net.gtamps.shared.serializer.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryConverterTest {

	@Test
		public void testWriteFloatToBytes() {
			byte[] b = new byte[2048];
			ArrayPointer inputpointer = new ArrayPointer();
			ArrayPointer outputpointer = new ArrayPointer();
			for (int input = -10000; input < 10000; input += 1344) {
				float floatinput = (float)(input+0.234234);
	//			System.out.println("input "+floatinput);
				BinaryConverter.writeFloatToBytes(floatinput, b, inputpointer);
	//			 for(int j=0; j<b.length; j++){
	//			 System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
	//			 System.out.println("b"+j+": "+b[j]);
	//			 }
				 
	//			 System.out.println("end: "+BinaryPrimitiveConverter.byteToFloat(b));
				
				assertEquals(floatinput, BinaryConverter.readFloatFromBytes(b, outputpointer),0.0f);
			}
		}

	@Test
		public void testWriteIntToBytes() {
			byte[] b = new byte[2048];
			ArrayPointer inputpointer = new ArrayPointer();
			ArrayPointer outputpointer = new ArrayPointer();
			for (int input = -10000; input < 10000; input += 1344) {
				// System.out.println("input "+input);
				BinaryConverter.writeIntToBytes(input, b, inputpointer);
				// for(int j=0; j<b.length; j++){
				// System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
				// System.out.println("b"+j+": "+b[j]);
				// }
				// System.out.println("end: "+Integer.toBinaryString(BinaryPrimitiveConverter.byteToInt(b)));
				// System.out.println("end: "+BinaryPrimitiveConverter.byteToInt(b));
				assertEquals(input, BinaryConverter.readIntFromBytes(b, outputpointer));
			}
		}

	@Test
		public void testWriteLongToBytes() {
			byte[] b = new byte[2048];
			ArrayPointer inputpointer = new ArrayPointer();
			ArrayPointer outputpointer = new ArrayPointer();
			for (int input = -10000; input < 10000; input += 1344) {
				// System.out.println("input "+input);
				BinaryConverter.writeLongToBytes(input, b, inputpointer);
				// for(int j=0; j<b.length; j++){
				// System.out.println("b"+j+": "+Integer.toBinaryString(b[j]));
				// System.out.println("b"+j+": "+b[j]);
				// }
				// System.out.println("end: "+Integer.toBinaryString(BinaryPrimitiveConverter.byteToInt(b)));
				// System.out.println("end: "+BinaryPrimitiveConverter.byteToInt(b));
				assertEquals(input, BinaryConverter.readLongFromBytes(b, outputpointer));
			}
		}
	
	@Test
	public void testCharArrayToBytes() {
		String s = "eabcæ?ß";
		String s2 = "oiausd";
		ArrayPointer inputpointer = new ArrayPointer();
		ArrayPointer outputpointer = new ArrayPointer();
		byte[] b = new byte[2048];
		BinaryConverter.writeStringToBytes(s, b, inputpointer);
		BinaryConverter.writeStringToBytes(s2, b, inputpointer);
//		for (int j = 0; j < b.length; j++) {
//			System.out.println("b" + j + ": " + Integer.toBinaryString(b[j]));
//			System.out.println("b" + j + ": " + b[j]);
//		}
//		System.out.println(BinaryPrimitiveConverter.byteToCharArray(b));
		assertEquals(s, BinaryConverter.readStringFromBytes(b, outputpointer));
		assertEquals(s2, BinaryConverter.readStringFromBytes(b, outputpointer));
	}

}
