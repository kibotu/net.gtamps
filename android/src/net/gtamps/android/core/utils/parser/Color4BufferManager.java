package net.gtamps.android.core.utils.parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import net.gtamps.android.core.utils.Color4;


public class Color4BufferManager {

	public static final int PROPERTIES_PER_ELEMENT = 4;
	public static final int BYTES_PER_PROPERTY = 1;

	private ByteBuffer buffer;
	private int numElements;
	
	public Color4BufferManager(ByteBuffer buffer, int size) {
		this.buffer = ByteBuffer.allocate(buffer.limit() * BYTES_PER_PROPERTY);
		this.buffer.put(buffer);
		numElements = size;
	}

	public Color4BufferManager(int maxElements) {
		int numBytes = maxElements * PROPERTIES_PER_ELEMENT * BYTES_PER_PROPERTY;
		buffer = ByteBuffer.allocateDirect(numBytes);
		buffer.order(ByteOrder.nativeOrder());
	}
	
	/**
	 * The number of items in the list. 
	 */
	public int size()	{
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
		return new Color4( buffer.get(), buffer.get(), buffer.get(), buffer.get() );
	}
	
	public void putInColor4(int index, Color4 color4) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		color4.r = (short)buffer.get();
		color4.g = (short)buffer.get();
		color4.b = (short)buffer.get();
		color4.a = (short)buffer.get();
	}

	public short getPropertyR(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		return (short)buffer.get();
	}
	public short getPropertyG(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
		return (short)buffer.get();
	}
	public float getPropertyB(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
		return (short)buffer.get();
	}
	public float getPropertyA(int index) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 3);
		return (short)buffer.get();
	}
	
	public void add(Color4 c) {
        add(c.r,c.g,c.b,c.a);
	}
	
	public void add(short r, short g, short b, short a) {
		set(numElements, r, g, b, a);
		numElements++;
	}
	
	public void set(int index, Color4 color4)	{
        set(index,color4.r,color4.g,color4.b,color4.a);
	}

	public void set(int index, short r, short g, short b, short a)	{
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		buffer.put((byte)r);
		buffer.put((byte)g);
		buffer.put((byte)b);
		buffer.put((byte)a);
	}
	
	public void setPropertyR(int index, short r) {
		buffer.position(index * PROPERTIES_PER_ELEMENT);
		buffer.put((byte)r);
	}
	public void setPropertyG(int index, short g) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 1);
		buffer.put((byte)g);
	}
	public void setPropertyB(int index, short b) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 2);
		buffer.put((byte)b);
	}
	public void setPropertyA(int index, short a) {
		buffer.position(index * PROPERTIES_PER_ELEMENT + 3);
		buffer.put((byte)a);
	}
	
	public ByteBuffer getByteBuffer() 	{
		return buffer;
	}

    public FloatBuffer getFloatBuffer() 	{
		return buffer.asFloatBuffer();
	}
	
	public Color4BufferManager clone()	{
		buffer.position(0);
		return new Color4BufferManager(buffer, numElements);
	}
}
