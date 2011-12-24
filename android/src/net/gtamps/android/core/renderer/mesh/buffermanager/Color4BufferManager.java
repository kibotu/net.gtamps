package net.gtamps.android.core.renderer.mesh.buffermanager;

import net.gtamps.shared.Utils.math.Color4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Color4BufferManager {

    public static final int PROPERTIES_PER_ELEMENT = 4;
    public static final int BYTES_PER_PROPERTY = 4;

    private FloatBuffer buffer;
    private int numElements = 0;

    public Color4BufferManager(FloatBuffer buffer, int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.limit() * BYTES_PER_PROPERTY);
        bb.order(ByteOrder.nativeOrder());
        this.buffer = bb.asFloatBuffer();
        this.buffer.put(buffer);
        numElements = size;
    }

    public Color4BufferManager(int maxElements) {
        int numBytes = maxElements * PROPERTIES_PER_ELEMENT * BYTES_PER_PROPERTY;
        ByteBuffer bb = ByteBuffer.allocateDirect(numBytes);
        bb.order(ByteOrder.nativeOrder());
        buffer = bb.asFloatBuffer();
    }

    /**
     * The number of items in the list.
     */
    public int size() {
        return numElements;
    }

    /**
     * The _maximum_ number of items that the list can hold, as defined on instantiation.
     * (Not to be confused with the Buffer's capacity)
     */
    public int capacity() {
        return buffer.capacity() / PROPERTIES_PER_ELEMENT;
    }

    /**
     * Clear object in preparation for garbage collection
     */
    public void clear() {
        buffer.clear();
    }

    public Color4 getAsColor4(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        return new Color4(buffer.get(), buffer.get(), buffer.get(), buffer.get());
    }

    public float getPropertyR(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        return buffer.get();
    }

    public float getPropertyG(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
        return buffer.get();
    }

    public float getPropertyB(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
        return buffer.get();
    }

    public float getPropertyA(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 3);
        return buffer.get();
    }

    public void add(Color4 c) {
        add(c.r, c.g, c.b, c.a);
    }

    public void add(float r, float g, float b, float a) {
        set(numElements, r, g, b, a);
        numElements++;
    }

    public void set(int index, Color4 color4) {
        set(index, color4.r, color4.g, color4.b, color4.a);
    }

    public void set(int index, float r, float g, float b, float a) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        buffer.put(r);
        buffer.put(g);
        buffer.put(b);
        buffer.put(a);
    }

    public void setPropertyR(int index, float r) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        buffer.put(r);
    }

    public void setPropertyG(int index, float g) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
        buffer.put(g);
    }

    public void setPropertyB(int index, float b) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
        buffer.put((byte) b);
    }

    public void setPropertyA(int index, float a) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 3);
        buffer.put(a);
    }

    public FloatBuffer getBuffer() {
        return buffer;
    }

    public Color4BufferManager clone() {
        buffer.position(0);
        return new Color4BufferManager(buffer, numElements);
    }
}
