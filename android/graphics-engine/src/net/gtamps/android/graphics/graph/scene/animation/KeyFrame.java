package net.gtamps.android.graphics.graph.scene.animation;

import net.gtamps.android.graphics.graph.scene.primitives.Object3D;

/**
 * User: Jan Rabe
 * Date: 18/11/12
 * Time: 14:34
 */
public class KeyFrame {

    protected String id;
    protected Object3D object3D;

    public KeyFrame(String id, Object3D object3D) {
        this.id = id;
        this.object3D = object3D;
    }

    public Object3D getObject3D() {
        return object3D;
    }
}
