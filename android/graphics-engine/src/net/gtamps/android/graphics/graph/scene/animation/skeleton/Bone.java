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

    private Bone parent;
    public String name;
    public int flag; // 2 if it is a root, 0 else.
    public int nameHash;
    private Vector3 position;
    private Vector3 rotation;
    private Vector3 scaling;
    private Vector3 pivot;

    public Bone() {
        parent = null;
        position = Vector3.createNew();
        rotation = Vector3.createNew();
    }

    public void setRotation(Quaternion q) {
        setRotationFromQuaternion(q.x,q.y,q.z,q.w);
    }

    public void setRotationFromQuaternion(float x, float y, float z, float w) {
        rotation.setEulerAnglesFromQuaterion(x,y,z,w);
    }
}
