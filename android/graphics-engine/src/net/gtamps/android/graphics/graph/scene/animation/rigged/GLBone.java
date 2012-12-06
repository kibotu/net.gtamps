package net.gtamps.android.graphics.graph.scene.animation.rigged;

import net.gtamps.shared.Utils.math.Matrix4;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bone from a specific key frame in an animation.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 14:05
 */
public class GLBone implements Comparable<GLBone> {

    public static final int ROOT = 65535;

    public String name;
    public int id;
    public int parent;

    public Matrix4 transform;
    public List<Matrix4> frames;

    public GLBone() {
        name = "";
        // -1 reserved for root
        parent = -2;

        transform = Matrix4.createNew();
        frames = new ArrayList<Matrix4>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GLBone)) return false;

        GLBone glBone = (GLBone) o;

        if (id != glBone.id) return false;
        if (parent != glBone.parent) return false;
        if (frames != null ? !frames.equals(glBone.frames) : glBone.frames != null) return false;
        if (name != null ? !name.equals(glBone.name) : glBone.name != null) return false;
        if (transform != null ? !transform.equals(glBone.transform) : glBone.transform != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + parent;
        result = 31 * result + (transform != null ? transform.hashCode() : 0);
        result = 31 * result + (frames != null ? frames.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(GLBone o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this == o) return EQUAL;
        return this.id < o.id ? BEFORE : AFTER;
    }
}
