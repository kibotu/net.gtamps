package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.shared.Utils.math.Quaternion;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 15:58
 */
public class BoneKeyFrame {

    public int nameHash;
    public Quaternion rotation;
    public float x;
    public float y;
    public float z;

    public BoneKeyFrame() {
        this.rotation = new Quaternion();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoneKeyFrame)) return false;

        BoneKeyFrame that = (BoneKeyFrame) o;

        if (nameHash != that.nameHash) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nameHash;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("BoneKeyFrame");
        sb.append("{nameHash=").append(nameHash);
        sb.append(", rotation=").append(rotation);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append('}');
        return sb.toString();
    }
}
