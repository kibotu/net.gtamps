package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;

import java.util.ArrayList;

/**
 * User: Jan Rabe
 * Date: 23/11/12
 * Time: 17:04
 */
public class Bone {

    public short parentId;
    public String name;
    public short id;
    public int flag; // 2 if it is a root, 0 else.
    public int nameHash;
    public final Vector3 position;
    public final Vector3 rotation;
    public final Vector3 scaling;
    public final Vector3 pivot;

    public Bone() {
        position = Vector3.createNew();
        rotation = Vector3.createNew();
        scaling = Vector3.createNew();
        pivot = Vector3.createNew();
    }

    public void setEulerRotation(Quaternion q) {
        setRotationEulerRotation(q.x,q.y,q.z,q.w);
    }

    public void setRotationEulerRotation(float x, float y, float z, float w) {
        rotation.setEulerAnglesFromQuaterion(x,y,z,w);
    }
}
