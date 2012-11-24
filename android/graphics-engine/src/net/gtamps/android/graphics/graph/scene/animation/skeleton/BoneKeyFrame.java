package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 15:58
 */
public class BoneKeyFrame {

    public int nameHash;
    public float rot[];
    public float x;
    public float y;
    public float z;

    public BoneKeyFrame() {
        this.rot = new float[4];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoneKeyFrame)) return false;

        BoneKeyFrame that = (BoneKeyFrame) o;

        if (nameHash != that.nameHash) return false;
        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (Float.compare(that.z, z) != 0) return false;
        if (!Arrays.equals(rot, that.rot)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rot != null ? Arrays.hashCode(rot) : 0;
        result = 31 * result + (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        result = 31 * result + nameHash;
        return result;
    }
}
