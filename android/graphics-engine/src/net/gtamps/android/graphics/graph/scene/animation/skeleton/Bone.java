package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;

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
    public final Vector3 ct;
    public final Matrix4 combinedMatrix;
    public static final int ROOT_BONE_ID = 65535;
    public Bone parent;

    public Bone() {
        position = Vector3.createNew();
        rotation = new Quaternion();
        ct = Vector3.createNew();
        combinedMatrix = Matrix4.createNew();
        parent = null;
    }

    public Quaternion getRotation() {
        // Update orientation.
        // Append quaternions for rotation transform B * A.

        if(parent == null) return rotation;

        Quaternion result = new Quaternion(rotation);
        result.mulLeft(parent.getRotation());

        return result;
    }

    public Vector3 getPosition() {
        // Update position.
        // child.pos = parent.pos + v3.transform(child.pos, parent.rot)
       // child.position.transform(parent.rotation).add(parent.position);

        if(parent == null) return position;

        Vector3 result = Vector3.createNew(position);
        result.transform(parent.getRotation()).add(parent.getPosition());

        return result;
    }
}
