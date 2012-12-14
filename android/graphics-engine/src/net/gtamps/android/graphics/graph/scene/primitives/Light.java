package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.SceneNode;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.math.MathUtils;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:59
 */
public class Light extends SceneNode {

    private Vector3 direction = Vector3.createNew();

    public Light(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ) {
        setPosition(positionX, positionY, positionZ);
        direction.set(directionX, directionY, directionZ);
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

    @Override
    public void onCreate(GL10 gl10) {
    }

    @Override
    protected void onCreateInternal(GL10 gl10) {
    }

    @Override
    protected void onTransformationInternal(GL10 gl10, boolean isDirty) {
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Registry.getRenderer().applyLight(this);
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }

    @Override
    public void onResume(GL10 gl10) {
    }

    @Override
    protected void onResumeInternal(GL10 gl10) {
    }
}
