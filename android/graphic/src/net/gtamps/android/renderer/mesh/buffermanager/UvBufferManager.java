package net.gtamps.android.renderer.mesh.buffermanager;

import net.gtamps.android.renderer.mesh.Uv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class UvBufferManager {

    public static final int PROPERTIES_PER_ELEMENT = 2;
    public static final int BYTES_PER_PROPERTY = 4;

    private FloatBuffer buffer;
    private int numElements = 0;

    public UvBufferManager(FloatBuffer buffer, int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.limit() * BYTES_PER_PROPERTY);
        bb.order(ByteOrder.nativeOrder());
        this.buffer = bb.asFloatBuffer();
        this.buffer.put(buffer);
        numElements = size;
    }

    public UvBufferManager(int maxElements) {
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

    public Uv getAsUv(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        return new Uv(buffer.get(), buffer.get());
    }

    public void putInUv(int index, Uv uv) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        uv.u = buffer.get();
        uv.v = buffer.get();
    }

    public float getPropertyU(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        return buffer.get();
    }

    public float getPropertyV(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
        return buffer.get();
    }

    public void add(Uv uv) {
        add(uv.u, uv.v);
    }

    public void add(float u, float v) {
        set(numElements, u, v);
        numElements++;
    }

    public void set(int index, Uv uv) {
        set(index, uv.u, uv.v);
    }

    public void set(int index, float u, float v) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        buffer.put(u);
        buffer.put(v);
    }

    public void setPropertyU(int index, float u) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        buffer.put(u);
    }

    public void setPropertyV(int index, float v) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
        buffer.put(v);
    }

    public FloatBuffer getFloatBuffer() {
        return buffer;
    }

    public UvBufferManager clone() {
        buffer.position(0);
        return new UvBufferManager(buffer, numElements);
    }
}