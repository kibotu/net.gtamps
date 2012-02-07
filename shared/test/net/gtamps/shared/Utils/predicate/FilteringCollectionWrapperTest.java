package net.gtamps.shared.Utils.predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FilteringCollectionWrapperTest {

	private static final Predicate<Object> TRUE = Predicates.alwaysTrue();
	private static final Predicate<Object> FALSE = Predicates.alwaysFalse();

	private static Object[] BACKING_ARRAY = {new Object(), new Object(), new Object()};
	private static final int BACKING_COLLECTION_SIZE = BACKING_ARRAY.length;

	@Mock
	private Collection<Object> backingCollection;
	@Mock
	private Predicate<Object> filter;
	private FilteringCollectionWrapper<Object> testee;

	@Before
	public void createFilteringCollectionWrapper() throws Exception {
		testee = new FilteringCollectionWrapper<Object>(backingCollection);
		when(backingCollection.size()).thenReturn(BACKING_COLLECTION_SIZE);
		when(backingCollection.isEmpty()).thenReturn(BACKING_COLLECTION_SIZE > 0);
		when(backingCollection.iterator()).thenReturn(createBackingIterator());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testFilteringCollectionWrapper_whenNull_expectException() {
		new FilteringCollectionWrapper<Object>(null);
	}

	@Test
	public final void testFilteringCollectionWrapper_shouldHaveAlwaysTrueDefaultFilter() {
		assertSame(TRUE, testee.getFilter());
	}

	@Test
	public final void testSize_whenAlwaysTrueFilter_shouldEqualBackingSize() {
		assertEquals(BACKING_COLLECTION_SIZE, testee.withFilter(TRUE).size());
	}

	@Test
	public final void testSize_whenAlwaysFalseFilter_shouldEqualZero() {
		assertEquals(0, testee.withFilter(FALSE).size());
	}

	@Test
	public final void testSize_whenSpecificElementFilter_shouldEqualOne() {
		assertEquals(1, testee.withFilter(trueOnce()).size());
	}

	@Test
	public final void testIterator_whenEmptyBacking_shouldBeEmpty() {
		testee = new FilteringCollectionWrapper<Object>(emptyCollection());

		assertFalse(testee.iterator().hasNext());
	}

	@Test
	public final void testTrueForAll_whenEmpty_expectTrue() {
		testee = new FilteringCollectionWrapper<Object>(emptyCollection());
		assertTrue(testee.trueForAll());
	}

	@Test
	public final void testTrueForAll_whenTrueFilter_expectTrue() {
		assertTrue(testee.withFilter(TRUE).trueForAll());
	}

	@Test
	public final void testTrueForAll_whenFalseFilter_expectFalse() {
		assertFalse(testee.withFilter(FALSE).trueForAll());
	}

	@Test
	public final void testTrueForAll_whenSpecificElementFilter_expectFalse() {
		assertFalse(testee.withFilter(trueOnce()).trueForAll());
	}

	@Test
	public final void testTrueForOne_whenEmpty_expectFalse() {
		testee = new FilteringCollectionWrapper<Object>(emptyCollection());
		assertFalse(testee.trueForOne());
	}

	@Test
	public final void testTrueForOne_whenTrueFilter_expectTrue() {
		assertTrue(testee.withFilter(TRUE).trueForOne());
	}

	@Test
	public final void testTrueForOne_whenFalseFilter_expectFalse() {
		assertFalse(testee.withFilter(FALSE).trueForOne());
	}

	@Test
	public final void testTrueForOne_whenSpecificElementFilter_expectTrue() {
		assertTrue(testee.withFilter(trueOnce()).trueForOne());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testWithFilter_whenNull_expectException() {
		testee.withFilter(null);
	}

	@Test
	public final void testWithFilter_whenValid_expectCollectionWithThisFilter() {
		assertSame(filter, testee.withFilter(filter).getFilter());
	}

	@Test
	public final void testAdd_whenFilterRejectsElement_shouldReturnFalse(){
		assertFalse(testee.withFilter(FALSE).add(new Object()));
	}

	@Test
	public final void testAdd_whenFilterAcceptsElement_shouldDelegateToBackingCollection(){
		final Object newElement = new Object();

		testee.withFilter(TRUE).add(newElement);

		verify(backingCollection).add(newElement);
	}

	private Predicate<Object> trueOnce() {
		when(filter.appliesTo(anyObject())).thenReturn(true, false);
		return filter;
	}

	private Collection<Object> emptyCollection() {
		return Collections.emptySet();
	}

	private Iterator<Object> createBackingIterator() {
		return new Iterator<Object>() {

			int i = 0;

			@Override
			public boolean hasNext() {
				return i < BACKING_COLLECTION_SIZE;
			}

			@Override
			public Object next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return BACKING_ARRAY[i++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}};
	}

}
