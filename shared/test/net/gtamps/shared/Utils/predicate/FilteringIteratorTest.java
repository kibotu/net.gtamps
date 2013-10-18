package net.gtamps.shared.Utils.predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FilteringIteratorTest {

	@Mock
	private Iterator<Boolean> iterator;
	@Mock
	private Predicate<Boolean> filter;
	private FilteringIterator<Boolean> filteringIterator;

	@After
	public final void forceExplicitTesteeReset() {
		filteringIterator = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testFilteringIterator_whenFilterNull_expectException() {
		new FilteringIterator<Boolean>(null, iterator);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testFilteringIterator_whenBackingNull_expectException() {
		new FilteringIterator<Boolean>(filter, null);
	}

	@Test
	public final void testHasNext_whenBackingDoesNot_shouldReturnFalse() {
		spentIterator();
		createFilteringIterator();

		assertFalse(filteringIterator.hasNext());
	}

	@Test
	public final void testHasNext_whenBackingDoesButFilterWontAcceptAny_shouldReturnFalse() {
		twiceFalseIterator();
		createFilteringIterator();

		assertFalse(filteringIterator.hasNext());
	}

	@Test
	public final void testHasNext_whenBackingDoesAndFilterWillAccept_shouldReturnTrue() {
		unlimitedTrueIterator();
		truePassFilter();
		createFilteringIterator();

		assertTrue(filteringIterator.hasNext());
	}

	@Test
	public final void testHasNext_whenBackingDoesAndFilterWillAcceptLaterOne_shouldReturnTrue() {
		onceFalseThenTrueIterator();
		truePassFilter();
		createFilteringIterator();

		assertTrue(filteringIterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public final void testNext_whenNoNext_expectException() {
		spentIterator();
		createFilteringIterator();

		filteringIterator.next();
	}

	@Test
	public final void testNext_whenNext_shouldReturnProperElement() {
		onceFalseThenTrueIterator();
		truePassFilter();
		createFilteringIterator();

		assertEquals("filter returned wrong element", true, filteringIterator.next());
	}

	@Test
	public final void testRemove_shouldDelegate() {
		createFilteringIterator();

		filteringIterator.remove();

		verify(iterator).remove();
	}



	private void createFilteringIterator()  {
		filteringIterator = new FilteringIterator<Boolean>(filter, iterator);
	}

	private void truePassFilter() {
		when(filter.appliesTo(true)).thenReturn(true, true);
		when(filter.appliesTo(false)).thenReturn(false, false);
	}

	private void spentIterator() {
		when(iterator.hasNext()).thenReturn(false, false);
		when(iterator.next()).thenThrow(new NoSuchElementException(), new NoSuchElementException());
	}

	private void unlimitedTrueIterator() {
		when(iterator.hasNext()).thenReturn(true, true);
		when(iterator.next()).thenReturn(true, true);
	}

	private void twiceFalseIterator() {
		when(iterator.hasNext()).thenReturn(true, true, false);
		when(iterator.next()).thenReturn(false, false);
	}

	private void onceFalseThenTrueIterator() {
		when(iterator.hasNext()).thenReturn(true, true);
		when(iterator.next()).thenReturn(false, true);
	}

}
