package net.gtamps.shared.Utils.predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
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

	private static Object[] BACKING_ARRAY = {new Object(), new Object(), new Object()};
	private static final int BACKING_COLLECTION_SIZE = BACKING_ARRAY.length;
	@Mock
	private Collection<Object> backingCollection;

	private final Iterator<Object> backingIterator = new Iterator<Object>() {

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
		@Mock
		private Predicate<Object> filter;
		private FilteringCollectionWrapper<Object> testee;

		@Before
		public void createFilteringCollectionWrapper() throws Exception {
			testee = new FilteringCollectionWrapper<Object>(backingCollection);
			when(backingCollection.size()).thenReturn(BACKING_COLLECTION_SIZE);
			when(backingCollection.iterator()).thenReturn(backingIterator);
			when(backingCollection.isEmpty()).thenReturn(BACKING_COLLECTION_SIZE > 0);
		}

		@Test(expected = IllegalArgumentException.class)
		public final void testFilteringCollectionWrapper_whenNull_expectException() {
			new FilteringCollectionWrapper<Object>(null);
		}

		@Test
		public final void testSize_whenDefaultFilter_shouldEqualBackingSize() {
			assertEquals(BACKING_COLLECTION_SIZE, testee.size());
		}

		@Test
		public final void testSize_whenAlwaysTrueFilter_shouldEqualBackingSize() {
			assertEquals(BACKING_COLLECTION_SIZE, testee.withFilter(Predicates.alwaysTrue()).size());
		}

		@Test
		public final void testSize_whenAlwaysFalseFilter_shouldEqualZero() {
			assertEquals(0, testee.withFilter(Predicates.alwaysFalse()).size());
		}

		@Test
		public final void testSize_whenSpecificElementFilter_shouldEqualOne() {
			assertEquals(1, testee.withFilter(singleObjectFilter()).size());
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
		public final void testTrueForAll_whenDefaultFilter_expectTrue() {
			assertTrue(testee.trueForAll());
		}

		@Test
		public final void testTrueForAll_whenTrueFilter_expectTrue() {
			assertTrue(testee.withFilter(Predicates.alwaysTrue()).trueForAll());
		}

		@Test
		public final void testTrueForAll_whenFalseFilter_expectFalse() {
			assertFalse(testee.withFilter(Predicates.alwaysFalse()).trueForAll());
		}

		@Test
		public final void testTrueForAll_whenSpecificElementFilter_expectFalse() {
			assertFalse(testee.withFilter(singleObjectFilter()).trueForAll());
		}

		@Test
		public final void testTrueForOne_whenEmpty_expectFalse() {
			testee = new FilteringCollectionWrapper<Object>(emptyCollection());
			assertFalse(testee.trueForOne());
		}

		@Test
		public final void testTrueForOne_whenTrueFilter_expectTrue() {
			assertTrue(testee.withFilter(Predicates.alwaysTrue()).trueForOne());
		}

		@Test
		public final void testTrueForOne_whenFalseFilter_expectFalse() {
			assertFalse(testee.withFilter(Predicates.alwaysFalse()).trueForOne());
		}

		@Test
		public final void testTrueForOne_whenSpecificElementFilter_expectTrue() {
			assertTrue(testee.withFilter(singleObjectFilter()).trueForOne());
		}

		@Test(expected = IllegalArgumentException.class)
		public final void testWithFilter_whenNull_expectException() {
			testee.withFilter(null);
		}

		@Test
		public final void testWithFilter_whenValid_expectCollectionWithThisFilter() {
			assertSame(filter, testee.withFilter(filter).getFilter());
		}

		private Predicate<Object> singleObjectFilter() {
			when(filter.appliesTo(anyObject())).thenReturn(true, false);
			return filter;
		}

		private Collection<Object> emptyCollection() {
			return Collections.emptySet();
		}

}
