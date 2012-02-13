package net.gtamps.game.instantiation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import net.gtamps.game.instantiation.ReflectiveObjectBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ReflectiveObjectBuilderTest {

	private static final Object TEST_OBJECT = new Object();
	private static Collection<Object> TEST_COLLECTION = Collections.singleton(TEST_OBJECT);

	@SuppressWarnings("unused")
	static class A {
		private Object privateSuperField;
		protected Object protectedSuperField;
		Object packagevisibleSuperField;

		public A() {}
	}

	@SuppressWarnings("unused")
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
		public Collection<Object> initializedCollection = new ArrayList<Object>(Arrays.asList(TEST_OBJECT));

		private B() {
			super();
		}
	}


	private ReflectiveObjectBuilder<B> testBuilder;

	@Before
	public final void createObjectBuilder() throws InstantiationException, IllegalAccessException {
		testBuilder = new ReflectiveObjectBuilder<B>(B.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testObjectBuilderClass_whenNull_expectException() {
		new ReflectiveObjectBuilder<Object>(null);
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

	@Test
	public final void testExtend_whenSuperbuilderWithParams_shouldSetFieldsAccrodingly() {
		final ReflectiveObjectBuilder<A> superBuilder = new ReflectiveObjectBuilder<A>(A.class);
		superBuilder.setParam("privateSuperField", TEST_OBJECT);

		testBuilder.extend(superBuilder);

		assertSame(TEST_OBJECT, getFieldValue("privateSuperField", testBuilder.build()));
	}

	@Test
	public final void testExtend_whenSuperbuilderWithParamsAndSameParamSet_shouldOverrideSuperbuilder() {
		final ReflectiveObjectBuilder<A> superBuilder = new ReflectiveObjectBuilder<A>(A.class);
		superBuilder.setParam("privateSuperField", TEST_OBJECT);
		final Object expected = new Object();
		testBuilder.setParam("privateSuperField", expected);

		testBuilder.extend(superBuilder);

		assertSame(expected, getFieldValue("privateSuperField", testBuilder.build()));
	}

	@Test(expected = IllegalStateException.class)
	public final void testSetParam_whenLocked_shouldThrowException() {
		testBuilder.lock();
		testBuilder.setParam("publicField", TEST_OBJECT);
	}

	@Test
	public final void testSetParam_whenDeclaredField_shouldBuildObjectWithFieldSetCorrectly() {
		for (final Field field: B.class.getDeclaredFields()) {
			testField(field);
		}
	}

	@Test
	public final void testSetParam_whenSuperclassField_shouldBuildObjectWithFieldSetCorrectly() {
		for (final Field field: A.class.getDeclaredFields()) {
			testField(field);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testSetParam_whenFieldDoesntExist_expectException() {
		testBuilder.setParam("notAFieldName", TEST_OBJECT);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testSetParam_whenNullAndFieldIsPrimitive_expectException() {
		class X {
			@SuppressWarnings("unused")
			int i;
		}
		final ReflectiveObjectBuilder<X> xbuilder = new ReflectiveObjectBuilder<X>(X.class);

		xbuilder.setParam("i", null);
	}

	@Test(expected = IllegalStateException.class)
	public final void testSetParam_whenLocked_expectException() {
		testBuilder.lock();

		testBuilder.setParam("publicField", TEST_OBJECT);
	}

	private final void testField(final Field field) {
		final String name = field.getName();
		Object expectedValue = TEST_OBJECT;
		try {
			testBuilder.setParam(name, TEST_OBJECT);
		} catch (final IllegalArgumentException e) {
			expectedValue = TEST_COLLECTION;
			testBuilder.setParam(name, TEST_COLLECTION);
		}

		assertSame(name + ": ", expectedValue, getFieldValue(name, testBuilder.build()));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testAddListParam_whenFieldIsNoCollection_expectException() {
		testBuilder.addListParam("notACollectionField", TEST_OBJECT);
	}

	@Test
	public final void testAddListParam_whenFieldIsNotInitialized_shouldBeCreatedAndFilled() {
		testBuilder.addListParam("collectionField", TEST_OBJECT);

		assertCollectionSameElements(TEST_COLLECTION, testBuilder.build().collectionField);
	}

	@Test
	public final void testAddListParam_whenFieldIsInitialized_shouldBeAdded() {
		final Object additionalObject = new Object();
		testBuilder.addListParam("initializedCollection", additionalObject);

		assertCollectionSameElements(Arrays.asList(TEST_OBJECT, additionalObject), testBuilder.build().initializedCollection);
	}

	private <T> void assertCollectionSameElements(final Collection<T> c1, final Collection<T> c2) {
		if (c1.size() != c2.size()) {
			fail("collections must be same size");
		}
		final Iterator<T> iter = c2.iterator();
		for (final T e: c1) {
			assertEquals(e, iter.next());
		}
	}

	//TODO	more ListParamTests

	@Test
	public final void testBuild_whenNothingSet_allFieldsShouldContainNullValue() {
		final B built = testBuilder.build();

		for (final Field field: built.getClass().getFields()) {
			validateFieldNotInitialized(field, built);
		}
	}

	private void validateFieldNotInitialized(final Field field, final Object instance) {
		final B reference = new B();
		assertEquals(getFieldValue(field, reference), getFieldValue(field, instance));
	}

	private Object getFieldValue(final String name, final Object instance) {
		Class<?> type = instance.getClass();
		while (type != null) {
			try {
				return getFieldValue(type.getDeclaredField(name), instance);
			} catch (final NoSuchFieldException e) {
				type = type.getSuperclass();
			} catch (final SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private Object getFieldValue(final Field field, final Object instance) {
		Object value = null;
		final boolean isAccessible = field.isAccessible();
		if (!isAccessible) {
			field.setAccessible(true);
		}
		try {
			value = field.get(instance);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		if (!isAccessible) {
			field.setAccessible(false);
		}
		return value;
	}

}
