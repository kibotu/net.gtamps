package net.gtamps.android.renderer.utils;

import java.nio.*;
import java.util.List;

final public class OpenGLUtils {

    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_SHORT = 2;

    // static
    private OpenGLUtils() {
    }

    /**
     * Allocates a {@link java.nio.FloatBuffer} with <code>capacity</code> lenght.
     *
     * @param capacity
     * @return floatBuffer
     */
    public static FloatBuffer allocateFloatBuffer(final int capacity) {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
    }

    /**
     * Allocates a {@link java.nio.IntBuffer} with <code>capacity</code> lenght.
     *
     * @param capacity
     * @return intBuffer
     */
    public static IntBuffer allocateIntBuffer(final int capacity) {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asIntBuffer();
    }

    /**
     * Allocates a {@link java.nio.ShortBuffer} with <code>capacity</code> lenght.
     *
     * @param capacity
     * @return shortBuffer
     */
    public static ShortBuffer allocateShortBuffer(final int capacity) {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asShortBuffer();
    }

    /**
     * Puts a vertex into a {@link java.nio.FloatBuffer}.
     *
     * @param buffer
     * @param x
     * @param y
     * @param z
     */
    public static void addVertex3f(final FloatBuffer buffer, final float x,
                                   final float y, final float z) {
        buffer.put(x);
        buffer.put(y);
        buffer.put(z);
    }

    /**
     * Puts Indices into a {@link java.nio.ShortBuffer}.
     *
     * @param buffer
     * @param index1
     * @param index2
     * @param index3
     */
    public static void addIndex(final ShortBuffer buffer, final int index1,
                                final int index2, final int index3) {
        buffer.put((short) index1);
        buffer.put((short) index2);
        buffer.put((short) index3);
    }

    /**
     * Puts Texture Coordinates into a {@link java.nio.FloatBuffer}.
     *
     * @param buffer
     * @param u
     * @param v
     */
    public static void addCoord2f(final FloatBuffer buffer, final float u, final float v) {
        buffer.put(u);
        buffer.put(v);
    }

    /**
     * Puts a Color Material into a {@link java.nio.FloatBuffer}.
     *
     * @param buffer
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public static void addFloat4(final FloatBuffer buffer, final float r, final float g, final float b, final float a) {
        buffer.put(r);
        buffer.put(g);
        buffer.put(b);
        buffer.put(a);
    }

    public static void addFloat4PositionZero(final FloatBuffer buffer, final float r, final float g, final float b, final float a) {
        buffer.position(0);
        buffer.put(r);
        buffer.put(g);
        buffer.put(b);
        buffer.put(a);
        buffer.position(0);
    }

    public static void addFloat3PositionZero(final FloatBuffer buffer, final float x, final float y, final float z) {
        buffer.position(0);
        buffer.put(x);
        buffer.put(y);
        buffer.put(z);
        buffer.position(0);
    }

    /**
     * Allocates a Float Array {@link java.nio.FloatBuffer} with the capacity of
     * <code>values.length</code>.
     *
     * @param values
     * @return floatBuffer
     */
    public static FloatBuffer toFloatBufferPositionZero(final float[] values) {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(values.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        final FloatBuffer buffer = vbb.asFloatBuffer();
        buffer.put(values);
        buffer.position(0);
        return buffer;
    }

    /**
     * Allocates a Float Array {@link java.nio.FloatBuffer} with the capacity of
     * <code>values.length</code>.
     *
     * @param values
     * @return floatBuffer
     */
    public static FloatBuffer toFloatBufferPositionZero(final List<Float> values) {
        return toFloatBufferPositionZero(toPrimitive(values));
    }

    /**
     * Allocates a Short Array {@link java.nio.ShortBuffer} with the capacity of
     * <code>values.length</code>.
     *
     * @param values
     * @return shortBuffer
     */
    public static ShortBuffer toShortBuffer(final short[] values) {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(values.length * 2);
        vbb.order(ByteOrder.nativeOrder());
        final ShortBuffer buffer = vbb.asShortBuffer();
        buffer.put(values);
        buffer.position(0);
        return buffer;
    }

    /**
     * Allocates an Int Array {@link java.nio.IntBuffer} with the capacity of
     * <code>values.length</code>.
     *
     * @param values
     * @return IntBuffer
     */
    public static IntBuffer toIntBuffer(final int[] values) {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(values.length * 2);
        vbb.order(ByteOrder.nativeOrder());
        final IntBuffer buffer = vbb.asIntBuffer();
        buffer.put(values);
        buffer.position(0);
        return buffer;
    }

    /**
     * Uggly way of converting a list to a float primitive.
     *
     * @param list of floats
     * @return float []
     * @see <a href="http://stackoverflow.com/questions/4837568/java-convert-arraylistfloat-to-float">Converting ArrayList<Float> to float[]</a>
     */
    public static float[] toPrimitive(List<Float> list) {
        float[] floatArray = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Float f = list.get(i);
            floatArray[i] = f != null ? f : Float.NaN;
        }
        return floatArray;
    }

    /**
     * Convenience Method for creating a FloatBuffer out of 3 values.
     *
     * @param x
     * @param y
     * @param z
     * @return FloatBuffer with xyz.
     */
    public static FloatBuffer makeFloatBuffer3(float x, float y, float z) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3 * BYTES_PER_FLOAT);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuffer.asFloatBuffer();
        buffer.put(x);
        buffer.put(y);
        buffer.put(z);
        buffer.position(0);
        return buffer;
    }

    /**
     * Convenience Method for creating a FloatBuffer out of 3 values.
     *
     * @param r
     * @param g
     * @param b
     * @param a
     * @return FloatBuffer with rgba.
     */
    public static FloatBuffer makeFloatBuffer4(float r, float g, float b, float a) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * BYTES_PER_FLOAT);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuffer.asFloatBuffer();
        buffer.put(r);
        buffer.put(g);
        buffer.put(b);
        buffer.put(a);
        buffer.position(0);
        return buffer;
    }
}
