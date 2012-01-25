package net.gtamps.shared.serializer.communication;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class TranslatorTest {

	@Test
	public void testLookupString() {
		for(Field f : StringConstants.class.getFields()){
			try {
				String fieldStr = (String) f.get(null);
				Byte b = Translator.lookup(fieldStr);
				System.out.println(fieldStr+" "+Translator.lookup(fieldStr));
				assertEquals(fieldStr, Translator.lookup(b));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertEquals(-1, Translator.lookup("askdjasdkjh"));
		assertNotSame("asdoasd", Translator.lookup(2));
	}
}
