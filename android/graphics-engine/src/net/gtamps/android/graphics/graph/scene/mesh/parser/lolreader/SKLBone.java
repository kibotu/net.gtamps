package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

/**
 * Stores the contents of a bone from an .skl file.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:07
 */
public class SKLBone {

    public String name;
    public static final int BONE_NAME_SIZE = 32;
    public int ID;
    public int parentID;
    public float scale;
    public float[] position;
    public float[] orientation;
    public static final int ORIENTATION_SIZE = 12;

    public SKLBone() {
        name = "";
        ID = 0;
        parentID = 0;
        scale = 0.0f;
        position = new float[3];
        orientation = new float[ORIENTATION_SIZE];
    }
}
