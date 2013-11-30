package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the contents of an .anm file.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 09:52
 */
public class ANMFile {
    public static final int ID_SIZE = 8;
    public String id;

    public int version;

    public int magic;

    public int numberOfBones;
    public int numberOfFrames;

    public int playbackFPS;

    public List<ANMBone> bones;

    public ANMFile() {
        id = "";

        version = 0;

        magic = 0;

        numberOfBones = 0;
        numberOfFrames = 0;

        playbackFPS = 0;

        bones = new ArrayList<ANMBone>();
    }
}
