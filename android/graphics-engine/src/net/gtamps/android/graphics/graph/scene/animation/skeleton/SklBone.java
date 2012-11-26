package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.shared.Utils.math.Matrix4;

/**
 * User: Jan Rabe
 * Date: 25/11/12
 * Time: 15:18
 */
public class SklBone {

    public static final int kNameLen = 0x20;            // 32
    public static final int kSizeWithoutMatrix = 0x28;  // 40
    public static final int kSizeInFile = 0x58;         // 88

    public SklBone() {
        name = new char[kNameLen];
        scale = 0.1f;
        transform = Matrix4.createNew();
//        memset(name, 0, kNameLen);
    }

    public char [] name;
    public int parent;
    public float scale; // 0.1f
    public Matrix4 transform;
}
