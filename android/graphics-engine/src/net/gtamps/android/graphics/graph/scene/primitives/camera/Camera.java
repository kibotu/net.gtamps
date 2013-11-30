package net.gtamps.android.graphics.graph.scene.primitives.camera;

import android.graphics.Bitmap;
import net.gtamps.android.graphics.graph.SceneNode;
import net.gtamps.android.graphics.graph.scene.ViewPort;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.android.graphics.utils.Utils;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;
import net.gtamps.shared.Utils.math.MathUtils;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:59
 */
public class Camera extends SceneNode {

    /**
     * Der Sichtkegel
     */
    @NotNull
    private Frustum frustum = new Frustum(16f / 10f, 45.0f, 0.01f, 1000.0f);

    /**
     * Viewport.
     */
    @NotNull
    private ViewPort viewport = new ViewPort(0, 0, 800, 480);

    /**
     * Defines how it the view will be rendered.
     * <code>true</code> for perspective
     * <code>false</code> for orthographic
     */
    private boolean isPersectiveView = true;
    private Color4 backgroundColor = Color4.BLACK;

    /**
     * Constructs a new camera object.
     *
     * @param positionX
     * @param positionY
     * @param positionZ
     * @param targetX
     * @param targetY
     * @param targetZ
     * @param upX
     * @param upY
     * @param upZ
     */
    public Camera(float positionX, float positionY, float positionZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
        this(positionX, positionY, positionZ, targetX, targetY, targetZ, upX, upY, upZ, 45);
    }

    /**
     * Sets a new Camera.
     *
     * @param positionX
     * @param positionY
     * @param positionZ
     * @param targetX
     * @param targetY
     * @param targetZ
     * @param upX
     * @param upY
     * @param upZ
     * @param fovy
     */
    public Camera(float positionX, float positionY, float positionZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ, int fovy) {
        setPosition(positionX, positionY, positionZ);
        frustum.setCamera(this.position, Vector3.createNew(targetX, targetY, targetZ), Vector3.createNew(upX, upY, upZ));
        frustum.setHorizontalFieldOfView(fovy, 1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Registry.getRenderer().clearScreen(backgroundColor);
        Registry.getRenderer().setActiveCamera(this);
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
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
    public void onResume(GL10 gl10) {
    }

    @Override
    protected void onResumeInternal(GL10 gl10) {
    }

    public void onSurfaceChanged(GL10 gl10, ViewPort viewPort) {
        viewport.setViewPort(viewPort);
        frustum.setAspectRatio(viewport.getAspectRatio());
        if (isPersectiveView) frustum.setPerspectiveProjection();
        else frustum.setOrthographicProjection();
        viewport.applyViewPort();
    }

    public void setPerspectiveView() {
        isPersectiveView = true;
    }

    public void setOrthographicView() {
        isPersectiveView = false;
    }

    public void move(float x, float y, float z) {
        frustum.move(x,y,z, true);
    }

    public float getDistanceToViewField() {
        return frustum.getDistanceToVieField();
    }

    public float getHorizontalFieldOfViewEffective() {
        return frustum.getHorizontalFieldOfViewEffective();
    }

    public void setZoomFactor(float distance) {
        if(distance != 0) frustum.setZoomFactor(distance);
    }

    public void setBackgroundColor(Color4 color) {
        this.backgroundColor = color;
    }

    public void rotateAroundVector(Vector3 target, Vector3 angle, float distance) {

        // Calculate the camera position using the distance and angles
        float camX = (float) (distance * -Math.sin(angle.x* MathUtils.DEG_TO_RAD) * Math.cos((angle.y)*MathUtils.DEG_TO_RAD));
        float camY = (float) (distance * -Math.sin((angle.y) * MathUtils.DEG_TO_RAD));
        float camZ = (float) (-distance * Math.cos((angle.x) * MathUtils.DEG_TO_RAD) * Math.cos((angle.y) * MathUtils.DEG_TO_RAD));

        // Set the camera position and lookat point
        frustum.setCamera( camX,camY+0.5f,camZ, target.x, target.y+0.5f, target.z, 0, 1, 0);
    }

    @NotNull
    public ViewPort getViewport() {
        return viewport;
    }

    @NotNull
    public Frustum getFrustum() {
        return frustum;
    }
}
