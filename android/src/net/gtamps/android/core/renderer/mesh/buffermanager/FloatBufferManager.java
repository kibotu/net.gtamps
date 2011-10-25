package net.gtamps.android.core.renderer.mesh.buffermanager;

import net.gtamps.shared.math.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class FloatBufferManager {

	public static final int PROPERTIES_PER_ELEMENT = 3;
	public static final int BYTES_PER_PROPERTY = 4;

	private FloatBuffer buffer;
	private int numElements = 0;

	public FloatBufferManager(FloatBuffer buffer, int size) {
		ByteBuffer bb = ByteBuffer.allocateDirect(buffer.limit() * BYTES_PER_PROPERTY);
		bb.order(ByteOrder.nativeOrder());
		this.buffer = bb.asFloatBuffer();
		this.buffer.put(buffer);
		numElements = size;
	}

	public FloatBufferManager(int maxElements) {
		int numBytes = maxElements * PROPERTIES_PER_ELEMENT * BYTES_PER_PROPERTY;
		ByteBuffer bb = ByteBuffer.allocateDirect(numBytes);
		bb.order(ByteOrder.nativeOrder());
		buffer  = bb.asFloatBuffer();
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

	public Vector3 getVector3At(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		return Vector3.createNew(buffer.get(), buffer.get(), buffer.get());
	}

	public void putInNumber3d(int index, Vector3 vector3) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
        vector3.set(buffer.get(),buffer.get(),buffer.get());
	}

	public float getPropertyX(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		return buffer.get();
	}
	public float getPropertyY(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
		return buffer.get();
	}
	public float getPropertyZ(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
		return buffer.get();
	}

	public void add(Vector3 vector3) {
		set(numElements, vector3);
		numElements++;
	}

	public void add(float x, float y, float z) {
		set(numElements, x,y,z);
		numElements++;
	}

	public void set(int index, Vector3 vector3) {
        set(index, vector3.x, vector3.y, vector3.z);
	}

	public void set(int index, float x, float y, float z) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);
	}

	public void setPropertyX(int index, float x) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		buffer.put(x);
	}
	public void setPropertyY(int index, float y) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
		buffer.put(y);
	}
	public void setPropertyZ(int index, float z) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
		buffer.put(z);
	}

	public FloatBuffer getBuffer() {
		return buffer;
	}

	public void overwrite(float[] newValues)	{
		buffer.position(0);
		buffer.put(newValues);
	}

    @Override
	public FloatBufferManager clone() {
		buffer.position(0);
		FloatBufferManager c = new FloatBufferManager(buffer, numElements);
		return c;
	}
}