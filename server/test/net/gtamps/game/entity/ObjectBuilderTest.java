package net.gtamps.game.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;



public class ObjectBuilderTest {

	private static final Object TEST_OBJECT = new Object();
	private static final <T extends Collection<Object>> Collection<Object> createTestCollection(final Class<T> type) {
		try {
			return type.getConstructor().newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	static class A {
		private Object privateSuperField;
		protected Object protectedSuperField;
		Object packagevisibleSuperField;

		public A() {}
	}

	static class B extends A {
		public Object publicField;
		private Object privateField;
		Object packagevisibleField;
		public final Object finalField = null;
		public Object implicitlyNullableField;
		@Nullable Object explicitlyNullableField;
		@NotNull Object explicitlyNonnullField;
		public Object notACollectionField;
		public Collection<Object> collectionField;
		public Collection<Object> initializedCollection = Arrays.asList(TEST_OBJECT);

		//		private B() {
		//			super();
		//		}
	}


	private ObjectBuilder<B> testBuilder;

	@Before
	public final void createObjectBuilder() throws InstantiationException, IllegalAccessException {
		testBuilder = new ObjectBuilder<B>();
		testBuilder.classobject(B.class);
	}

	@Test
	public final void testIsLocked_whenLocked_shouldReturnTrue() {
		testBuilder.lock();
		assertTrue(testBuilder.isLocked());
	}

	@Test
	public final void testIsLocked_whenNotLocked_shouldReturnFalse() {
		testBuilder.unlock();
		assertFalse(testBuilder.isLocked());
	}

	@Test
	public final void testLock_whenUnlocked_shouldLock() {
		testBuilder.unlock();
		testBuilder.lock();
		assertTrue(testBuilder.isLocked());
	}

	@Test
	public final void testUnlock_whenLocked_shouldUnlock() {
		testBuilder.lock();
		testBuilder.unlock();
		assertFalse(testBuilder.isLocked());
	}

	@Test
	public final void testGetDescription_whenNull_shoouldReturnEmptyString() {
		testBuilder.desciption(null);
		assertEquals("", testBuilder.getDescription());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testClassobject_whenNull_shouldThrowException() {
		testBuilder.classobject(null);
	}

	@Test
	public final void testExtend() {
		fail("Not yet implemented"); // TODO
	}

	@Test(expected = IllegalStateException.class)
	public final void testSetParam_whenLocked_shouldThrowException() {
		testBuilder.lock();
		testBuilder.setParam("publicField", TEST_OBJECT);
	}

	@Test
	public final void testSetParam_whenFieldExistsAndIsWritable_shouldInstantiate() {

	}

	@Test
	public final void testAddListParam() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testBuild() {
		fail("Not yet implemented"); // TODO
	}

}
