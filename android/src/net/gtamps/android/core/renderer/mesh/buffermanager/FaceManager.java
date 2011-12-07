package net.gtamps.android.core.renderer.mesh.buffermanager;

import net.gtamps.android.core.renderer.mesh.Face;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class FaceManager {

    public static final int PROPERTIES_PER_ELEMENT = 3;
    public static final int BYTES_PER_PROPERTY = 2;

    private ShortBuffer buffer;
    private int numElements;

    private int renderSubsetStartIndex = 0;
    private int renderSubsetLength = 1;
    private boolean renderSubsetEnabled = false;

    public FaceManager(ShortBuffer buffer, int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.limit() * BYTES_PER_PROPERTY);
        bb.order(ByteOrder.nativeOrder());
        this.buffer = bb.asShortBuffer();
        this.buffer.put(buffer);
        numElements = size;
    }

    public FaceManager(int maxElements) {
        ByteBuffer b = ByteBuffer.allocateDirect(maxElements * PROPERTIES_PER_ELEMENT * BYTES_PER_PROPERTY);
        b.order(ByteOrder.nativeOrder());
        buffer = b.asShortBuffer();
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

    public Face get(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        return new Face(buffer.get(), buffer.get(), buffer.get());
    }

    public void putInFace(int index, Face face) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        face.a = (short) buffer.get();
        face.b = (short) buffer.get();
        face.c = (short) buffer.get();
    }

    public short getPropertyA(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        return (short) buffer.get();
    }

    public short getPropertyB(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
        return (short) buffer.get();
    }

    public float getPropertyC(int index) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
        return (short) buffer.get();
    }

    /**
     * Enables rendering only a subset of faces (renderSubset must be set to true)
     * This mechanism could be expanded to render multiple 'subsets' of the list of faces...
     */
    public void renderSubsetStartIndex(int num) {
        renderSubsetStartIndex = num;
    }

    public int renderSubsetStartIndex() {
        return renderSubsetStartIndex;
    }

    public void renderSubsetLength(int num) {
        renderSubsetLength = num;
    }

    public int renderSubsetLength() {
        return renderSubsetLength;
    }

    /**
     * If true, GLRenderer will only draw the faces as defined by
     * renderSubsetStartIndex and renderSubsetLength
     */
    public boolean renderSubsetEnabled() {
        return renderSubsetEnabled;
    }

    public void renderSubsetEnabled(boolean b) {
        renderSubsetEnabled = b;
    }

    public void add(Face f) {
        add(f.a, f.b, f.c);
    }

    public void add(int a, int b, int c) {
        add((short) a, (short) b, (short) c);
    }

    public void add(short a, short b, short c) {
        set(numElements, a, b, c);
        numElements++;
    }

    public void set(int index, Face face) {
        set(index, face.a, face.b, face.c);
    }

    public void set(int index, short a, short b, short c) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        buffer.put(a);
        buffer.put(b);
        buffer.put(c);
    }

    public void setPropertyA(int index, short a) {
        buffer.position(index * PROPERTIES_PER_ELEMENT);
        buffer.put(a);
    }

    public void setPropertyB(int index, short b) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
        buffer.put(b);
    }

    public void setPropertyC(int index, short c) {
        buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
        buffer.put(c);
    }

    public ShortBuffer getBuffer() {
        return buffer;
    }

    public FaceManager clone() {
        buffer.position(0);
        return new FaceManager(buffer, numElements);
    }

    public void addQuad(int upperLeft, int upperRight, int lowerRight, int lowerLeft) {
        add((short) upperLeft, (short) lowerRight, (short) upperRight);
        add((short) upperLeft, (short) lowerLeft, (short) lowerRight);
    }
}