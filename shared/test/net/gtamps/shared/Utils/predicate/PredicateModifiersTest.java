package net.gtamps.shared.Utils.predicate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class PredicateModifiersTest {

	@Mock
	private Predicate<Boolean> TRUE;
	@Mock
	private Predicate<Boolean> FALSE;
	@Mock
	private Predicate<Boolean> IDENTITY;
	@Mock
	private Predicate<Boolean> NEGATION;

	@Before
	public void setup() {
		when(TRUE.appliesTo(anyBoolean())).thenReturn(true, true);
		when(FALSE.appliesTo(anyBoolean())).thenReturn(false, false);
		when(IDENTITY.appliesTo(true)).thenReturn(true, true);
		when(IDENTITY.appliesTo(false)).thenReturn(false, false);
		when(NEGATION.appliesTo(true)).thenReturn(false, false);
		when(NEGATION.appliesTo(false)).thenReturn(true, true);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testNot_whenNull_expectException() {
		PredicateModifiers.NOT.applyTo((Predicate<Object>)null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNot_whenEmpty_expectException() {
		PredicateModifiers.NOT.applyTo(new Predicate[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNot_whenMoreThanOne_expectException() {
		PredicateModifiers.NOT.applyTo(TRUE, TRUE);
	}

	@Test
	public void testNot_whenTautology_expectContradiction() {
		final Predicate<Boolean> result = PredicateModifiers.NOT.applyTo(TRUE);

		assertPredicatesEqual(FALSE, result);
	}

	@Test
	public void testNot_whenContradiction_expectTautology() {
		final Predicate<Boolean> result = PredicateModifiers.NOT.applyTo(FALSE);

		assertPredicatesEqual(TRUE, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAnd_whenNull_expectException() {
		PredicateModifiers.AND.applyTo((Predicate<Object>)null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAnd_whenEmpty_expectException() {
		PredicateModifiers.AND.applyTo(new Predicate[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAnd_whenNullInMoreThanOne_expectException() {
		PredicateModifiers.AND.applyTo(TRUE, null);
	}

	@Test
	public void testAnd_whenTautologyAndIdentity_expectIdentity() {
		final Predicate<Boolean> result = PredicateModifiers.AND.applyTo(TRUE, IDENTITY);

		assertPredicatesEqual(IDENTITY, result);
	}

	@Test
	public void testAnd_whenContradictionAndTautology_expectContradiction() {
		final Predicate<Boolean> result = PredicateModifiers.AND.applyTo(TRUE, FALSE);

		assertPredicatesEqual(FALSE, result);
	}

	@Test
	public void testAnd_whenIdentityAndNegation_expectContradiction() {
		final Predicate<Boolean> result = PredicateModifiers.AND.applyTo(IDENTITY, NEGATION);

		assertPredicatesEqual(FALSE, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOr_whenNull_expectException() {
		PredicateModifiers.OR.applyTo((Predicate<Object>)null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOr_whenEmpty_expectException() {
		PredicateModifiers.OR.applyTo(new Predicate[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOr_whenNullInMoreThanOne_expectException() {
		PredicateModifiers.OR.applyTo(TRUE, null);
	}

	@Test
	public void testOr_whenTautologyAndIdentity_expectTautology() {
		final Predicate<Boolean> result = PredicateModifiers.OR.applyTo(TRUE, IDENTITY);

		assertPredicatesEqual(TRUE, result);
	}

	@Test
	public void testOr_whenContradictionAndTautology_expectTautology() {
		final Predicate<Boolean> result = PredicateModifiers.OR.applyTo(TRUE, FALSE);

		assertPredicatesEqual(TRUE, result);
	}

	@Test
	public void testAnd_whenIdentityAndNegation_expectTautology() {
		final Predicate<Boolean> result = PredicateModifiers.OR.applyTo(IDENTITY, NEGATION);

		assertPredicatesEqual(TRUE, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testXor_whenNull_expectException() {
		PredicateModifiers.XOR.applyTo((Predicate<Object>)null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testXor_whenNullInMoreThanOne_expectException() {
		PredicateModifiers.XOR.applyTo(TRUE, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testXor_whenEmpty_expectException() {
		PredicateModifiers.XOR.applyTo(new Predicate[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testXor_whenSingle_expectException() {
		PredicateModifiers.XOR.applyTo(TRUE);
	}

	@Test
	public void testXor_whenTautologyXorContradiction_expectTautology() {
		final Predicate<Boolean> result = PredicateModifiers.XOR.applyTo(TRUE, FALSE);

		assertPredicatesEqual(TRUE, result);
	}

	@Test
	public void testXor_whenIdentityXorNegation_expectTautology() {
		final Predicate<Boolean> result = PredicateModifiers.XOR.applyTo(IDENTITY, NEGATION);

		assertPredicatesEqual(TRUE, result);
	}

	@Test
	public void testXor_whenIdentityXorIdentity_expectContradiction() {
		final Predicate<Boolean> result = PredicateModifiers.XOR.applyTo(IDENTITY, IDENTITY);

		assertPredicatesEqual(FALSE, result);
	}

	@Test
	public void testXor_whenTautologyXorTautology_expectContradiction() {
		final Predicate<Boolean> result = PredicateModifiers.XOR.applyTo(TRUE, TRUE);

		assertPredicatesEqual(FALSE, result);
	}

	@Test
	public void testXor_whenContradictionXorContradiction_expectContradiction() {
		final Predicate<Boolean> result = PredicateModifiers.XOR.applyTo(FALSE, FALSE);

		assertPredicatesEqual(FALSE, result);
	}

	@Test
	public void testXor_whenContradictionXorContradictionXorIdentity_expectIdentity() {
		final Predicate<Boolean> result = PredicateModifiers.XOR.applyTo(FALSE, FALSE, IDENTITY);

		assertPredicatesEqual(IDENTITY, result);
	}






	private void assertPredicatesEqual(final Predicate<Boolean> expected, final Predicate<Boolean> actual) {
		assertEquals("not equal for true", expected.appliesTo(true), actual.appliesTo(true));
		assertEquals("not equal for false", expected.appliesTo(false), actual.appliesTo(false));
	}



}
