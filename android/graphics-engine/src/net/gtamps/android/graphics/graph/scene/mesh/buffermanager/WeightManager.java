package net.gtamps.android.graphics.graph.scene.mesh.buffermanager;

import net.gtamps.android.graphics.graph.scene.mesh.Weight;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * User: Jan Rabe
 * Date: 28/11/12
 * Time: 10:00
 */
public class WeightManager {

    public static final int PROPERTIES_PER_ELEMENT = 4;
    public static final int BYTES_PER_PROPERTY = 4;

    private FloatBuffer weights;
    private IntBuffer influences;
    private int numElements = 0;

    public WeightManager(FloatBuffer weights,IntBuffer influences, int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(weights.limit() * BYTES_PER_PROPERTY);
        bb.order(ByteOrder.nativeOrder());
        this.weights = bb.asFloatBuffer();
        this.weights.put(weights);
        bb = ByteBuffer.allocateDirect(influences.limit() * BYTES_PER_PROPERTY);
        bb.order(ByteOrder.nativeOrder());
        this.influences = bb.asIntBuffer();
        this.influences.put(influences);
        numElements = size;
    }

    public WeightManager(int maxElements) {
        int numBytes = maxElements * PROPERTIES_PER_ELEMENT * BYTES_PER_PROPERTY;
        ByteBuffer bb = ByteBuffer.allocateDirect(numBytes);
        bb.order(ByteOrder.nativeOrder());
        weights = bb.asFloatBuffer();

        bb = ByteBuffer.allocateDirect(numBytes);
        bb.order(ByteOrder.nativeOrder());
        influences = bb.asIntBuffer();
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
        return weights.capacity() / PROPERTIES_PER_ELEMENT;
    }

    /**
     * Clear object in preparation for garbage collection
     */
    public void clear() {
        weights.clear();
    }

    public Weight getAsWeight(int index) {
        weights.position(index * PROPERTIES_PER_ELEMENT);
        return new Weight(weights.get(), weights.get(), weights.get(), weights.get(),
                influences.get(),influences.get(),influences.get(),influences.get());
    }

    public float getPropertyWeightX(int index) {
        weights.position(index * PROPERTIES_PER_ELEMENT);
        return weights.get();
    }

    public float getPropertyWeightY(int index) {
        weights.position(index * PROPERTIES_PER_ELEMENT + 1);
        return weights.get();
    }

    public float getPropertyWeightZ(int index) {
        weights.position(index * PROPERTIES_PER_ELEMENT + 2);
        return weights.get();
    }

    public float getPropertyWeightW(int index) {
        weights.position(index * PROPERTIES_PER_ELEMENT + 3);
        return weights.get();
    }

    public int getPropertyInfluence1(int index) {
        influences.position(index * PROPERTIES_PER_ELEMENT);
        return influences.get();
    }

    public int getPropertyInfluence2(int index) {
        influences.position(index * PROPERTIES_PER_ELEMENT + 1);
        return influences.get();
    }

    public int getPropertyInfluence3(int index) {
        influences.position(index * PROPERTIES_PER_ELEMENT + 2);
        return influences.get();
    }

    public int getPropertyInfluence4(int index) {
        influences.position(index * PROPERTIES_PER_ELEMENT + 3);
        return influences.get();
    }

    public void add(Weight weight) {
        add(weight.x, weight.y, weight.z, weight.w, weight.influence1, weight.influence2, weight.influence3, weight.influence4);
    }

    public void add(float x, float y, float z, float w, int i1, int i2, int i3, int i4) {
        set(numElements, x, y, z, w, i1, i2, i3, i4);
        numElements++;
    }

    public void set(int index, Weight weight) {
        set(index, weight.x, weight.y, weight.z, weight.w, weight.influence1, weight.influence2, weight.influence3, weight.influence4);
    }

    public void set(int index, float x, float y, float z, float w, int i1, int i2, int i3, int i4) {
        weights.position(index * PROPERTIES_PER_ELEMENT);
        weights.put(x);
        weights.put(y);
        weights.put(z);
        weights.put(w);
        influences.position(index * PROPERTIES_PER_ELEMENT);
        influences.put(i1);
        influences.put(i2);
        influences.put(i3);
        influences.put(i4);
    }

    public void setPropertyX(int index, float x) {
        weights.position(index * PROPERTIES_PER_ELEMENT);
        weights.put(x);
    }

    public void setPropertyY(int index, float y) {
        weights.position(index * PROPERTIES_PER_ELEMENT + 1);
        weights.put(y);
    }

    public void setPropertyZ(int index, float z) {
        weights.position(index * PROPERTIES_PER_ELEMENT + 2);
        weights.put(z);
    }

    public void setPropertyW(int index, float w) {
        weights.position(index * PROPERTIES_PER_ELEMENT + 3);
        weights.put(w);
    }

    public void setPropertyInfluence1(int index, int i1) {
        influences.position(index * PROPERTIES_PER_ELEMENT + 1);
        influences.put(i1);
    }

    public void setPropertyInfluence2(int index, int i2) {
        influences.position(index * PROPERTIES_PER_ELEMENT + 2);
        influences.put(i2);
    }

    public void setPropertyInfluence3(int index, int i3) {
        influences.position(index * PROPERTIES_PER_ELEMENT + 3);
        influences.put(i3);
    }

    public void setPropertyInfluence4(int index, int i4) {
        influences.position(index * PROPERTIES_PER_ELEMENT);
        influences.put(i4);
    }

    public void overwrite(float[] newWeights, int [] newInfluences) {
        weights.position(0);
        weights.put(newWeights);
        influences.position(0);
        influences.put(newInfluences);
    }

    public FloatBuffer getBufferWeights() {
        return weights;
    }

    public IntBuffer getBufferInfluences() {
        return influences;
    }

    public WeightManager clone() {
        weights.position(0);
        influences.position(0);
        return new WeightManager(weights,influences, numElements);
    }
}
