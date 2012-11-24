package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import java.util.ArrayList;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 15:58
 */
public class BoneKeyFrame {
    public float rot[] = new float[4];
    public float x;
    public float y;
    public float z;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("BoneKeyFrame");
        sb.append("{rot=").append(rot == null ? "null" : "");
        for (int i = 0; rot != null && i < rot.length; ++i)
            sb.append(i == 0 ? "" : ", ").append(rot[i]);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append('}');
        return sb.toString();
    }
}
