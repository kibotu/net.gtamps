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
    protected int interpolation;

    public KeyFrame(String id, Object3D object3D, int interpolation) {
        this.id = id;
        this.object3D = object3D;
        this.interpolation = interpolation;
    }

    public Object3D getObject3D() {
        return object3D;
    }

    public String getId() {
        return id;
    }

    public int getInterpolation() {
        return interpolation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyFrame)) return false;

        KeyFrame keyFrame = (KeyFrame) o;

        if (interpolation != keyFrame.interpolation) return false;
        if (id != null ? !id.equals(keyFrame.id) : keyFrame.id != null) return false;
        if (object3D != null ? !object3D.equals(keyFrame.object3D) : keyFrame.object3D != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (object3D != null ? object3D.hashCode() : 0);
        result = 31 * result + interpolation;
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("KeyFrame");
        sb.append("{id='").append(id).append('\'');
        sb.append(", object3D=").append(object3D);
        sb.append(", interpolation=").append(interpolation);
        sb.append('}');
        return sb.toString();
    }
}
