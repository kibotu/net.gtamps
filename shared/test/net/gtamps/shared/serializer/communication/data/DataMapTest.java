package net.gtamps.shared.serializer.communication.data;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DataMapTest {

	private static final String TESTENTRY_KEY = "testentry";
	DataMap dataMap;

	@Before
	public final void setup() {
		dataMap = new DataMap();
	}

	@Ignore
	@Test
	public final void testGetMap() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testGetList() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testGetLong() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testGetInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetFloat() {
		final MapEntry<Value<Float>> entry = new MapEntry<Value<Float>>(TESTENTRY_KEY, new Value<Float>(123.45f)); 
		dataMap.add(entry);

		assertEquals(entry.value().get(), dataMap.getFloat(TESTENTRY_KEY), 0f); 
	}

	@Ignore
	@Test
	public final void testGetBoolean() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testGetString() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testGet() {
		fail("Not yet implemented"); // TODO
	}

}
