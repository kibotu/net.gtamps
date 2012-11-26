package net.gtamps.android.graphics.graph.scene.mesh.parser;

/**
 * User: Jan Rabe
 * Date: 26/11/12
 * Time: 22:25
 */
public class SknVertex {

    public static final int kSizeInFile = 0x50; // 80

    public SknVertex() {
        weights = new float[4];
        normal = new float[3];
        sknIndices = new char[4];
        sklIndices = new int[4];
    }

    public float x;
    public float y;
    public float z;
    public char [] sknIndices;
    public float [] weights;
    public float [] normal;
    public float u;
    public float v;
    public int [] sklIndices;
    public int uvIndex;
    public int dupeDataIndex;
}
