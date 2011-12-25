package net.gtamps.shared.Utils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MovingFloatAverageTest {

    private static final float TEST_TOLERANCE = 1e-6f;

    private final int frameSize = 3;
    private MovingFloatAverage movingFloatAverage;

    @Before
    public void createMovingFloatAverage() throws Exception {
        movingFloatAverage = new MovingFloatAverage(frameSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovingFloatAverage_whenZeroSize_shouldThrowException() {
        new MovingFloatAverage(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovingFloatAverage_whenSubZeroSize_shouldThrowException() {
        new MovingFloatAverage(-1);
    }

    @Test
    public void testMovingFloatAverage_shouldStartWithZero() {
        assertEquals(0f, movingFloatAverage.getAverage(), TEST_TOLERANCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_whenNull_shouldThrowException() {
        movingFloatAverage.addValue(null);
    }

    @Test
    public void testAddValue_shouldAdjustAverageAccordingly() {
        final float[] values = getValues(frameSize + 2);
        for (int i = frameSize; i < values.length; i++) {
            testAddValue(i, values);
        }
    }

    /**
     * creates a float array of frameSize + count values,
     * padded with 0s in front to account for 0-initialized frame
     */
    private float[] getValues(final int count) {
        assert count > 0;
        final float[] values = new float[frameSize + count];
        for (int i = frameSize; i < values.length; i++) {
            values[i] = i;
        }
        return values;
    }

    private void testAddValue(final int valIndex, final float[] values) {
        // setup
        int sumIndex = valIndex;
        float movingSum = 0;
        for (int i = 0; i < frameSize; i++, sumIndex--) {
            movingSum += values[sumIndex];
        }
        final float expected = movingSum / frameSize;

        // run
        movingFloatAverage.addValue(values[valIndex]);

        // assert
        assertEquals(expected, movingFloatAverage.getAverage(), TEST_TOLERANCE);
    }

    @Test
    public void testToString() {
        assertNotNull(movingFloatAverage.toString());
    }

    @Ignore
    @Test
    public void testHashCode() {
        fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

}
