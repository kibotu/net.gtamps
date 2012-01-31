package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.SceneNode;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:59
 */
public class Camera extends SceneNode {

    public Camera(float positionX, float positionY, float positionZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }

    @Override
    protected void onTransformation(GL10 gl10) {
    }

    @Override
    protected void onTransformationInternal(GL10 gl10) {
    }

    @Override
    public void onCreate(GL10 gl10) {
    }

    @Override
    protected void onCreateInternal(GL10 gl10) {
    }

    @Override
    public void onResume(GL10 gl10) {
    }

    @Override
    protected void onResumeInternal(GL10 gl10) {
    }

    public void onSurfaceChanged(GL10 gl10, int x, int y, int width, int height) {
    }
}
