package net.gtamps.android.core.renderer.graph.scene.primitives;

import android.opengl.GLES20;
import android.opengl.GLU;
import net.gtamps.android.core.renderer.RenderCapabilities;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.shader.Shader;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Frustum;
import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;


/**
 * Kameraknoten
 */
public class Camera extends RenderableNode {

    /**
     * Der Sichtkegel
     */
    @NotNull
    private Frustum frustum = new Frustum(16f / 10f, 45.0f, 0.01f, 1000.0f);

    /**
     * Viewport-Koordinaten
     */
    @NotNull
    private Vector3 viewportCoords = Vector3.createNew(0, 0, 0);

    private Vector3 viewPortDimension = Vector3.createNew(800, 480, 0);

    protected Matrix4 projectionMatrix = Matrix4.createNew();
    protected Matrix4 viewMatrix = Matrix4.createNew();
    protected Matrix4 normalMatrix = Matrix4.createNew();

    private boolean hasDepthTest = true;

    /**
     * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
     *
     * @param frustum Der Sichtkegel
     */
    public Camera(@NotNull Frustum frustum) {
        this.frustum = frustum;
    }

    /**
     * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
     *
     * @param position Die Position der Kamera
     * @param lookAt   Der Punkt, auf den die Kamera blickt
     * @param up       Der Hoch-Vektor der Kamera
     */
    public Camera(@NotNull Vector3 position, @NotNull Vector3 target, @NotNull Vector3 up) {
        define(position, target, up);
    }

    /**
     * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
     *
     * @param positionX X-Komponente des Positionsvektors
     * @param positionY Y-Komponente des Positionsvektors
     * @param positionZ Z-Komponente des Positionsvektors
     * @param eyeX      X-Komponente des Sichtvektors
     * @param eyeY      Y-Komponente des Sichtvektors
     * @param eyeZ      Z-Komponente des Sichtvektors
     * @param upX       X-Komponente des Hochvektors
     * @param upY       Y-Komponente des Hochvektors
     * @param upZ       Z-Komponente des Hochvektors
     * @see <a href="http://assets.stupeflix.com/code/images/cartesian.png">coordinate system</a>
     */
    public Camera(float positionX, float positionY, float positionZ, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ) {
        define(positionX, positionY, positionZ,
                eyeX, eyeY, eyeZ,
                upX, upY, upZ);
    }

    /**
     * Liefert eine Referenz auf die Viewport-Koordinaten (2D)
     *
     * @return Die Koordinaten (2D)
     */
    @NotNull
    public Vector3 getViewportCoords() {
        return viewportCoords;
    }

    /**
     * Liefert eine Referenz auf die Viewport-Dimensionen (2D)
     *
     * @return Die Dimensionen (2D)
     */
    @NotNull
    public Vector3 getViewportSize() {
        return dimension;
    }

    /**
     * Aspektratio ermitteln
     *
     * @return
     */
    public float getAspectRatio() {
        return frustum.getAspectRatio();
    }

    /**
     * Liefert den Winkel des horizontalen Sichtbereiches unter Beachtung des Zoomfaktors
     *
     * @return Der Winkel in Radians
     */
    public float getHorizontalFieldOfViewEffective() {
        return frustum.getHorizontalFieldOfViewEffective();
    }

    /**
     * Bezieht den Sichtkegel
     *
     * @return
     */
    @NotNull
    public Frustum getFrustum() {
        return frustum;
    }

    /**
     * Setzt den Sichtkegel
     *
     * @param frustum Der Kegel
     */
    @Deprecated
    public void setFrustum(@NotNull Frustum frustum) {
        this.frustum = frustum;
        setPosition(frustum.getPosition()); // TODO: Referenz verwenden
    }

    /**
     * Setzt die Kameraparameter
     *
     * @param position Die Position der Kamera
     * @param lookAt   Der Punkt, auf den die Kamera blickt
     * @param up       Der Hoch-Vektor der Kamera
     * @see #define(float, float, float, float, float, float, float, float, float)
     */
    public void define(@NotNull Vector3 position, @NotNull Vector3 lookAt, @NotNull Vector3 up) {
        this.position.set(position);
        frustum.setCamera(this.position, lookAt, up);
    }

    /**
     * Setzt die Kameraparameter
     *
     * @param positionX X-Komponente des Positionsvektors
     * @param positionY Y-Komponente des Positionsvektors
     * @param positionZ Z-Komponente des Positionsvektors
     * @param eyeX      X-Komponente des Sichtvektors
     * @param eyeY      Y-Komponente des Sichtvektors
     * @param eyeZ      Z-Komponente des Sichtvektors
     * @param upX       X-Komponente des Hochvektors
     * @param upY       Y-Komponente des Hochvektors
     * @param upZ       Z-Komponente des Hochvektors
     */
    public void define(float positionX, float positionY, float positionZ, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ) {
        position.set(positionX, positionY, positionZ);
        frustum.setCamera(position, Vector3.createNew(eyeX, eyeY, eyeZ), Vector3.createNew(upX, upY, upZ));
    }

    /**
     * Rotates the camera (hopefully correctly)
     *
     * @param angle
     * @param x
     * @param y
     * @param z
     */
    public void rotate(float angle, float x, float y, float z) {
        frustum.rotate(angle, x, y, z);
    }

    /**
     * Spezifische Implementierung der Aktualisierungslogik
     *
     * @param deltat Zeitdifferenz zum vorherigen Frame
     */
    @Override
    protected void updateInternal(float deltat) {
        // Nur nötig, wenn Kamera animiert wird. --> Pfade setviewzen, ...
        frustum.setPosition(position);
    }

    /**
     * Spezifische Implementierung des Verarbeitungsvorganges
     *
     * @param state Die State-Referenz
     */
    @Override
    protected void processInternal(@NotNull ProcessingState state) {
    }

    private void shadeInternalGLES20() {

        Vector3 pos = frustum.getPosition();
        Vector3 target = frustum.getTarget();
        Vector3 up = frustum.getUp();
        Matrix4.setLookAt(viewMatrix, pos, target, up);
//        Matrix.setLookAtM(viewMatrix.values,0,pos.x,pos.y,pos.z,target.x,target.y,target.z,up.x,up.y,up.z);

        if (hasDepthTest) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        } else {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        }

        int program = Shader.Type.PHONG.shader.getProgram();

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "projectionMatrix"), 1, false, projectionMatrix.values, 0);
        Logger.checkGlError(this, "projectionMatrix");

        // Create the normal modelview matrix
        // Invert + transpose of mvpmatrix
//        Matrix.invertM(normalMatrix.values, 0, projectionMatrix.values, 0);
//        Matrix.transposeM(normalMatrix.values, 0, normalMatrix.values, 0);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "normalMatrix"), 1, false, normalMatrix.values, 0);
        Logger.checkGlError(this, "normalMatrix");

        // eye view matrix
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "viewMatrix"), 1, false, viewMatrix.values, 0);
        Logger.checkGlError(this, "viewMatrix");
    }

    public void onSurfaceChanged(GL10 gl10, int x, int y, int width, int height) {
        viewportCoords.set(x, y, 0);
        viewPortDimension.set(width, height, 0);
        frustum.setAspectRatio(viewPortDimension.x/viewPortDimension.y);
        if (RenderCapabilities.supportsGLES20()) setViewportGLES20();
        else setViewportGL10(gl10);
        Logger.v(this, "[width:" + width + "| height:" + height + "| aspect:" + frustum.getAspectRatio() + "]");
    }

    private void setViewportGLES20() {
//        frustum.setOrthographicProjection(projectionMatrix);
        frustum.setPerspectiveProjection(projectionMatrix);
        GLES20.glViewport((int) viewportCoords.x, (int) viewportCoords.y, (int) dimension.x, (int) dimension.y);
        Logger.checkGlError(this, "glViewPort");
        setDirtyFlag();
    }

    private void setViewportGL10(GL10 gl10) {
        gl10.glViewport((int) viewportCoords.x, (int) viewportCoords.y, (int) dimension.x, (int) dimension.y);
        setDirtyFlag();
    }

    private void shadeInternalGL10(@NotNull GL10 gl) {
        gl.glViewport((int) viewportCoords.x, (int) viewportCoords.y, (int) dimension.x, (int) dimension.y);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, getHorizontalFieldOfViewEffective(), frustum.getAspectRatio(), frustum.getNearDistance(), frustum.getFarDistance());
        GLU.gluLookAt(gl, position.x, position.y, position.z, frustum.getTarget().x, frustum.getTarget().y, frustum.getTarget().z, frustum.getUp().x, frustum.getUp().y, frustum.getUp().z);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (hasDepthTest) {
            gl.glEnable(GL10.GL_DEPTH_TEST);
        } else {
            gl.glDisable(GL10.GL_DEPTH_TEST);
        }
    }

    private void shadeInternalGL10(@NotNull ProcessingState state) {
        shadeInternalGL10(state.getGl());
    }

    @Override
    public void shadeInternal(@NotNull ProcessingState state) {
        if (RenderCapabilities.supportsGLES20()) {
            shadeInternalGLES20();
        } else {
            shadeInternalGL10(state);
        }
    }

    private ProcessingState glState = new ProcessingState();

    /**
     * Spezifische Implementierung des Rendervorganges
     *
     * @param gl Die OpenGL-Referenz
     */
    @Override
    protected void renderInternal(@NotNull GL10 gl) {
        glState.setGl(gl);
        shadeInternal(glState);
    }

    @Override
    public RenderableNode getStatic() {
        return this;
    }

    @Override
    protected void setOptions() {
    }

    /**
     * Spezifische Implementierung des Bereinigungsvorganges
     *
     * @param state Die State-Referenz
     */
    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
        // Sollte nicht nötig sein
    }

    /**
     * Spezifische Implementierung des Setupvorganges
     *
     * @param state Die State-Referenz
     */
    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        // Sollte nicht nötig sein
    }

    /**
     * Setzt den Sichtpunkt der Kamera absolut, ohne die Position zu verändern
     *
     * @param point Der Zielpunkt
     */
    public void setTarget(@NotNull Vector3 point) {
        frustum.setTarget(point);
    }

    /**
     * Liefert den Sichtpunkt der Kamera absolut, ohne die Position zu verändern
     *
     * @see CameraNode#setTarget(de.widemeadows.summercamp.math.Vector3)
     */
    @NotNull
    public Vector3 getTarget() {
        return frustum.getTarget();
    }

    @Override
    public void move(float x, float y, float z) {
        move(x, y, z, true);
    }

    @Override
    public void move(@NotNull Vector3 position) {
        move(position, true);
    }

    /**
     * Verschiebt die Kamera (und ggf. den Sichtpunkt) relativ um den angegebenen Wert.
     *
     * @param offset     Der Wert
     * @param moveTarget Gibt an, ob das Ziel auch verschoben werden soll
     */
    public void move(@NotNull Vector3 offset, boolean moveTarget) {
        frustum.move(offset, moveTarget);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        frustum.setPosition(position);
    }

    public void setPosition(@NotNull Vector3 position) {
        setPosition(position.x, position.y, position.z);
    }

    /**
     * Verschiebt die Kamera (und ggf. den Sichtpunkt) relativ um den angegebenen Wert.
     *
     * @param offsetX    Wert der X-Bewegung
     * @param offsetY    Wert der Y-Bewegung
     * @param offsetZ    Wert der Z-Bewegung
     * @param moveTarget Gibt an, ob das Ziel auch verschoben werden soll
     */
    public void move(float offsetX, float offsetY, float offsetZ, boolean moveTarget) {
        frustum.move(offsetX, offsetY, offsetZ, moveTarget);
    }

    /**
     * Setzt den Zoomfaktor und prüft auf "unmögliche/ungewollte Zoomwerte"
     *
     * @param factor Der Faktor
     */
    public void setZoomFactor(float factor) {
        assert factor > 0;
        if (frustum.getHorizontalFieldOfView() / factor >= Config.MIN_ZOOM && frustum.getHorizontalFieldOfView() / factor <= Config.MAX_ZOOM) {
            frustum.setHorizontalFieldOfView(frustum.getHorizontalFieldOfView(), factor);
        }
    }

    /**
     * Liefert den Zoomfaktor
     *
     * @return Der Zoomfaktor
     */
    public float getZoomFactor() {
        return frustum.getZoomFactor();
    }

    /**
     * Setzt den horizontalen Blickwinkel
     *
     * @param fovAngleRadians Der Blickwinkel in Radians
     * @param resetZoom       Gibt an, ob der Zoom zurückgesetzt werden soll
     */
    public void setFieldOfViewHorizontal(float fovAngleRadians, boolean resetZoom) {
        frustum.setHorizontalFieldOfView(fovAngleRadians, resetZoom);
    }

    /**
     * Setzt den horizontalen Blickwinkel, ohne den Zoom zurückzusetzen
     *
     * @param fovAngleRadians Der Blickwinkel in Radians
     */
    public void setFieldOfViewHorizontal(float fovAngleRadians) {
        frustum.setHorizontalFieldOfView(fovAngleRadians, false);
    }

    @Override
    public void onDirty() {
    }

    public void enableDepthTest(boolean enableDepthTest) {
        hasDepthTest = enableDepthTest;
    }

    @Override
    public void afterProcess(ProcessingState state) {
        // do nothing
    }
}