package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

/**
 * Abrstraction for a vertex in a .skn file.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:02
 */
public class SKNVertex {

    public float[] position;
    public int[] boneIndex;
    public float[] weights;
    public float[] normal;
    public float[] uv;

    public static final int BONE_INDEX_SIZE = 4; // in bytes

    public SKNVertex() {
        position = new float[3];
        boneIndex = new int[BONE_INDEX_SIZE];
        weights = new float[4];
        normal = new float[3];
        uv = new float[2];
    }
}
