package net.gtamps.shared.Utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class MovingFloatAverage implements MovingAverage<Float> {

    private final BlockingQueue<Float> frame;
    private final int frameSize;
    private float average = 0f;

    public MovingFloatAverage(final int frameSize) {
        if (frameSize <= 0) {
            throw new IllegalArgumentException("frameSize must be > 0");
        }
        this.frameSize = frameSize;
        this.frame = new LinkedBlockingQueue<Float>(frameSize);
        initFrame();
        assert frame.size() == frameSize;
    }

    private void initFrame() {
        while (frame.offer(0f)) {
            // nothing left to do
        }
    }

    @Override
    public void addValue(final Float value) {
        if (value == null) {
            throw new IllegalArgumentException("'value' must not be 'null'");
        }
        final boolean changed = add(value);
        assert changed : "moving avg frame did not change on add";
    }

    private boolean add(final float valueToAdd) {
        final float valueRemoved = frame.poll();
        final boolean changed = frame.offer(valueToAdd);
        final float change = valueToAdd - valueRemoved;
        average += change / frameSize;
        return changed;
    }

    @Override
    public Float getAverage() {
        return average;
    }

    @Override
    public String toString() {
        return "MovingFloatAverage [" + average + ", frameSize=" + frameSize + ", " + frame + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + frameSize;
        result = prime * result + Float.floatToIntBits(average);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovingFloatAverage other = (MovingFloatAverage) obj;
        if (frameSize != other.frameSize) {
            return false;
        }
        if (Float.floatToIntBits(average) != Float.floatToIntBits(other.average)) {
            return false;
        }
        return true;
    }


}
