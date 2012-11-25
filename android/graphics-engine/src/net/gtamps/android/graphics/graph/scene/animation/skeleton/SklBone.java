package net.gtamps.android.graphics.graph.scene.animation.skeleton;

/**
 * User: Jan Rabe
 * Date: 25/11/12
 * Time: 15:18
 */
public class SklBone {

    public static final int kNameLen = 0x20;
    public static final int kSizeWithoutMatrix = 0x28;
    public static final int kSizeInFile = 0x58;

    public SklBone() {
        name = new char[kNameLen];
        scale = 0.1f;
        transform = new float[4][4];
//        memset(name, 0, kNameLen);
    }

    public char [] name;
    public int parent;
    public float scale; // 0.1f
    public float [][] transform;
}
