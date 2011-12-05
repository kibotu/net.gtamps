package net.gtamps.shared.configuration;

import static org.junit.Assert.*;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractConfigLiteralTest {

	/** mock implementation */
	private class MockAbstractConfigLiteral extends AbstractConfigLiteral<Integer> {
		MockAbstractConfigLiteral(final Integer value, final AbstractConfigSource source) {
			super(value, source);
		}
		@Override
		public Class<?> getType() {
			return java.lang.Object.class;
		}
		@Override
		public AbstractConfigLiteral<Integer> clone() {
			return new MockAbstractConfigLiteral(value, source);
		}
	}

	@Mock
	private AbstractConfigSource source;

	private final Integer value = 99;
	private AbstractConfigLiteral abstractConfigLiteral;

	@Before
	public void createAbstractConfigLiteral() throws Exception {
		abstractConfigLiteral = new MockAbstractConfigLiteral(value, source);
	}

	@Test
	public void testHashCode_whenSameValue_shouldReturnSameHash() {
		assertNotSame("test precondition violated: mock implementation clone() must return different object",
				abstractConfigLiteral, abstractConfigLiteral.clone());

		final AbstractConfigLiteral<?> same = abstractConfigLiteral.clone();

		assertEquals(abstractConfigLiteral.hashCode(), same.hashCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAbstractConfigLiteral_whenSourceNull_expectException() {
		new MockAbstractConfigLiteral(value, null);
	}

	@Test
	public void testGetSource_shouldReturnSourceInConstructor() {
		assertSame(source, abstractConfigLiteral.getSource());
	}

	@Test
	public void testGetKeys_shouldReturnSingletonCollectionContaining0() {
		assertEquals(1, abstractConfigLiteral.getKeys().size());
		assertEquals("0", abstractConfigLiteral.getKeys().iterator().next());
	}

	@Test
	public void testGetCount_shouldReturn1() {
		assertEquals(1, abstractConfigLiteral.getCount());
	}

	@Test
	public void testSelectString_when0_shouldReturnSelf() {
		assertEquals(abstractConfigLiteral, abstractConfigLiteral.select("0"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSelectString_whenNot0_expectException() {
		abstractConfigLiteral.select("x");
	}

	@Test
	public void testSelectInt_when0_shouldReturnSelf() {
		assertEquals(abstractConfigLiteral, abstractConfigLiteral.select(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSelectInt_whenNot0_expectException() {
		abstractConfigLiteral.select(1);
	}

	@Test
	public void testGetString_whenNullValue_shouldNotReturnNull() {
		assertNotNull(new MockAbstractConfigLiteral(null, source).getString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetInt_expectException() {
		abstractConfigLiteral.getInt();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFloat_expectException() {
		abstractConfigLiteral.getFloat();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetBoolean_expectException() {
		abstractConfigLiteral.getBoolean();
	}

	@Test
	public void testIterator_shouldReturnIteratorContainingOnlySelf() {
		final Iterator<?> iter = abstractConfigLiteral.iterator();
		assertTrue(iter.hasNext());
		assertEquals(abstractConfigLiteral, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testEqualsObject_whenSame_shouldReturnTrue() {
		assertTrue(abstractConfigLiteral.equals(abstractConfigLiteral));
	}

	@Test
	public void testEqualsObject_whenEqualValue_shouldReturnTrue() {
		assertTrue(abstractConfigLiteral.equals(new MockAbstractConfigLiteral(value, source)));
	}

	@Test
	public void testEqualsObject_whenDifferentValue_shouldReturnFalse() {
		assertFalse(abstractConfigLiteral.equals(new MockAbstractConfigLiteral(value+1, source)));
	}

	@Test
	public void testEqualsObject_whenDifferentType_shouldReturnFalse() {
		assertFalse(abstractConfigLiteral.equals(new Object()));
	}

	@Test
	public void testEqualsObject_whenNull_shouldReturnFalse() {
		assertFalse(abstractConfigLiteral.equals(null));
	}

}
