package net.gtamps.android.graphics.graph.scene.mesh.skeleton;

import net.gtamps.shared.Utils.math.Vector3;

/**
 * User: Jan Rabe
 * Date: 23/11/12
 * Time: 17:04
 */
public class Bone {

    private Bone parent;
    private int id;
    private Vector3 translation;
    private Vector3 rotation;

    public Bone() {
        parent = null;
        int id = 0;
        translation = Vector3.createNew();
        rotation = Vector3.createNew();
    }
}
