package net.gtamps.shared.game.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScoreTest {

	private static final EventType OTHER_EVENT_TYPE = EventType.ACTION_HANDBRAKE;
	final static Score.ScoreType TEST_TYPE = Score.ScoreType.FRAGS;
	final static Score.ScoreType DIFFERENT_TYPE = Score.ScoreType.DUMMY;

	@Mock
	GameObject someGameObject;

	private Score score;

	@Before
	public void before() throws Exception {
		score = new Score(TEST_TYPE);
		when(someGameObject.getUid()).thenReturn(0);
	}

	@Test
	public final void testScore_shouldCreateDummyScore() {
		assertEquals(Score.ScoreType.DUMMY, new Score().getType());
	}

	@Test
	public final void testScore_shouldCreateScoreWithZeroCount() {
		assertEquals(0, new Score().getCount());
	}

	@Test
	public final void testScoreScoreType_shouldCreateScoreOfGivenType() {
		assertEquals(TEST_TYPE, new Score(TEST_TYPE).getType());
	}

	@Test
	public final void testScoreScoreType_shouldCreateScoreWithZeroCount() {
		assertEquals(0, new Score(TEST_TYPE).getCount());
	}

	@Test
	public final void testGetName_shouldEqualTypeName() {
		assertEquals(TEST_TYPE.name(), new Score(TEST_TYPE).getName());
	}

	@Test
	public final void testClone_shouldReturnScoreWithEqualValues() {
		final Score clone = score.clone();

		assertEquals("type: ", score.getType(), clone.getType());
		assertEquals("count: ", score.getCount(), clone.getCount());
		assertEquals("modTime: ", score.getLastModificationTimestamp(), clone.getLastModificationTimestamp());
	}

	@Test
	public final void testClone_shouldReturnScoreWithIndependentValues() {
		final Score clone = score.clone();
		assert score.getType() != DIFFERENT_TYPE : "precondition violated";

		clone.setType(DIFFERENT_TYPE);
		assertTrue("type: ", score.getType() != clone.getType());

		clone.increaseCountBy(1);
		assertTrue("count: ", score.getCount() != clone.getCount());

		clone.setModificationTimeStamp(clone.getLastModificationTimestamp() + 1);
		assertTrue("modTime: ", score.getLastModificationTimestamp() != clone.getLastModificationTimestamp());
	}

	@Test
	public final void testIsMoreRecentThan_whenChangedAtDifferentTimes_shouldEvaluateCorrectly() {
		final Score otherScore = score.clone();
		waitForAFewMillisecondsToPass();
		otherScore.increaseCountBy(1);

		assertTrue(otherScore.isMoreRecentThan(score));
		assertFalse(score.isMoreRecentThan(otherScore));
	}

	@Test
	public final void testIsMoreRecentThan_whenChangedAtSameTimes_shouldbeFalseForBoth() {
		final Score otherScore = score.clone();

		assertFalse(otherScore.isMoreRecentThan(score));
		assertFalse(score.isMoreRecentThan(otherScore));
	}

	@Test
	public final void testGetEventType_whenNoDummy_shouldReturnEventTypeDefinedByScoreType() {
		assert TEST_TYPE != Score.ScoreType.DUMMY : "test precondition violated";
		assertEquals(score.getType().getTriggerEventType(), score.getEventType());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testGetEventType_whenDummy_expectException() {
		new Score().getEventType();
	}

	@Test
	public final void testReceiveEvent_whenTriggerEvent_shouldIncrementCount() {
		final EventType triggerType = score.getEventType();
		final int expectedCount = score.getCount() + 1;
		score.receiveEvent(new GameEvent(triggerType, someGameObject));

		assertEquals(expectedCount, score.getCount());
	}

	@Test
	public final void testReceiveEvent_whenTriggerEvent_shouldIncreaseLastModifiedTimeStamp() {
		final EventType triggerType = score.getEventType();
		final long previousTimestamp = score.getLastModificationTimestamp();
		waitForAFewMillisecondsToPass();

		score.receiveEvent(new GameEvent(triggerType, someGameObject));

		assertTrue("expecting increased timestamp", previousTimestamp < score.getLastModificationTimestamp());
	}

	@Test
	public final void testReceiveEvent_whenNotTriggerEvent_shouldNotChangeCount() {
		final EventType otherType = OTHER_EVENT_TYPE;
		assert !otherType.isType(score.getEventType()) : "event type should be unrelated to score.getEventType()";
		final int expectedCount = score.getCount();

		score.receiveEvent(new GameEvent(otherType, someGameObject));

		assertEquals(expectedCount, score.getCount());
	}

	@Test
	public final void testReceiveEvent_whenNotTriggerEvent_shouldNotChangeLastModifiedTimeStamp() {
		final EventType otherType = OTHER_EVENT_TYPE;
		final long expectedTimestamp = score.getLastModificationTimestamp();
		waitForAFewMillisecondsToPass();

		score.receiveEvent(new GameEvent(otherType, someGameObject));

		assertEquals(expectedTimestamp, score.getLastModificationTimestamp());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testReceiveEvent_whenNull_expectException() {
		score.receiveEvent(null);
	}

	@Test
	public final void testSetType_shouldChangeType() {
		final Score score = new Score(TEST_TYPE);

		score.setType(DIFFERENT_TYPE);

		assertEquals(DIFFERENT_TYPE, score.getType());
	}

	@Test
	public final void testIncreaseCount_whenPositive_shouldIncreaseCountAndTimestamp() {
		final int increase = 1;
		final int expectedCount = score.getCount() + increase;
		final long previousTimestamp = score.getLastModificationTimestamp();
		waitForAFewMillisecondsToPass();

		score.increaseCountBy(increase);

		assertEquals(expectedCount, score.getCount());
		assertTrue(previousTimestamp < score.getLastModificationTimestamp());
	}

	@Test
	public final void testIncreaseCount_whenZero_shouldNotChangeCountNorTimestamp() {
		final int expectedCount = score.getCount();
		final long expectedTimestamp = score.getLastModificationTimestamp();
		waitForAFewMillisecondsToPass();

		score.increaseCountBy(0);

		assertEquals(expectedCount, score.getCount());
		assertEquals(expectedTimestamp, score.getLastModificationTimestamp());

	}

	@Test(expected = IllegalArgumentException.class)
	public final void testIncreaseCount_whenNegative_expectException() {
		score.setCount(1); // make count stay valid when decreased
		score.increaseCountBy(-1);	// still expect exception
	}

	@Test
	public final void testSetCount_whenDifferentValue_shouldIncreaseModificationTimestamp() {
		final long previousTimestamp = score.getLastModificationTimestamp() + 1;
		final int newCount = score.getCount() + 1;
		waitForAFewMillisecondsToPass();

		score.setCount(newCount);

		assertTrue(previousTimestamp < score.getLastModificationTimestamp());
	}

	@Test
	public final void testSetCount_whenSameValue_shouldKeepModificationTimestamp() {
		final long previousTimestamp = score.getLastModificationTimestamp();
		waitForAFewMillisecondsToPass();

		score.setCount(score.getCount());

		assertEquals(previousTimestamp, score.getLastModificationTimestamp());
	}

	@Test
	public final void testSetModificationTimeStamp_shouldChangeTheTimestamp() {
		final long expected = score.getLastModificationTimestamp() + 1;

		score.setModificationTimeStamp(expected);

		assertEquals(expected, score.getLastModificationTimestamp());
	}

	@Test
	public final void testMergeScore_whenNull_shouldReturnSameScore() {
		assertSame(score, score.mergeScore(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testMergeScore_whenDifferentType_expectException() {
		score.mergeScore(new Score(DIFFERENT_TYPE));
	}

	@Test
	public final void testMergeScore_whenSameType_shouldAddCounts() {
		final Score otherScore = new Score(score.getType());
		final int firstCount = 1;
		final int secondCount = 2;
		score.setCount(firstCount);
		otherScore.setCount(secondCount);

		score.mergeScore(otherScore);

		assertEquals(firstCount + secondCount, score.getCount());
	}

	@Test
	public final void testMergeScore_whenSameType_shouldKeepModifiedTimestampOfMostRecent() {
		waitForAFewMillisecondsToPass();
		final Score otherScore = new Score(score.getType());

		score.mergeScore(otherScore);

		assertEquals(otherScore.getLastModificationTimestamp(), score.getLastModificationTimestamp());
	}

	private void waitForAFewMillisecondsToPass() {
		try {
			Thread.sleep(10);
		} catch (final InterruptedException e) {
		}
	}
}
