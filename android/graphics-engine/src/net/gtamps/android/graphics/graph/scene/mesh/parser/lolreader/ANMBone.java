package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the contents of a bone from an .anm file.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:10
 */
public class ANMBone {
    public static final int BONE_NAME_LENGTH = 32;
    public String name;
    public int id;
    public List<ANMFrame> frames;

    public ANMBone() {
        name = "";
        id = 0;
        frames = new ArrayList<ANMFrame>();
    }
}
