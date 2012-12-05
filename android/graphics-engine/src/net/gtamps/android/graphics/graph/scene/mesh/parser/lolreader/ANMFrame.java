package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

/**
 * Stores the position and orientation of an .anm bone for a give frame.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:11
 */
public class ANMFrame {

    public float[] orientation;
    public float[] position;

    public ANMFrame() {
        orientation = new float[4];
        position = new float[3];
    }
}
