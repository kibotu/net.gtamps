package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores the contents of an .skl file.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 09:52
 */
public class SKLFile {
    public static final int ID_SIZE = 8;
    public String id;

    public int version;
    public int designerID;

    public int numBones;
    public List<SKLBone> bones;

    public int numBoneIDs;
    public List<Integer> boneIDs;

    // Maps .skl bone ID's to version 4 .anm bone ID's.
    public HashMap<Integer, Integer> boneIDMap;

    public SKLFile() {
        id = "";

        version = designerID = numBones = 0;
        bones = new ArrayList<SKLBone>();

        numBoneIDs = 0;
        boneIDs = new ArrayList<Integer>();

        boneIDMap = new HashMap<Integer, Integer>();
    }
}
