package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;

import java.util.ArrayList;

/**
 * User: Jan Rabe
 * Date: 23/11/12
 * Time: 17:04
 */
public class Bone {

    public int parentID;
    public String name;
    public int id;
    public int flag; // 2 if it is a root, 0 else.
    public int nameHash;
    public final Vector3 position;
    public final Quaternion rotation;
    public final Vector3 pivot;
    public final Matrix4 combinedMatrix;
    public static final int ROOT_BONE_ID = 65535;

    public Bone() {
        position = Vector3.createNew();
        rotation = new Quaternion();
        pivot = Vector3.createNew();
        combinedMatrix = Matrix4.createNew();
    }
}
