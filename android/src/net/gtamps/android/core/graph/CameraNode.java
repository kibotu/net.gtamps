package net.gtamps.android.core.graph;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.core.math.Frustum;
import net.gtamps.android.core.math.Matrix4;
import net.gtamps.android.core.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * Kameraknoten
 */
public class CameraNode extends RenderableNode {

    public static final String TAG = CameraNode.class.getSimpleName();

	/**
	 * Der Sichtkegel
	 */
	@NotNull
	private Frustum frustum = new Frustum(16f/10f, 45.0f, 0.01f, 1000.0f);

    /**
     * Viewport-Koordinaten
     */
    @NotNull
    private Vector3 viewportCoords = Vector3.createNew(0, 0, 0);

    /**
     * Viewport-Ausmaße
     */
    @NotNull
    private Vector3 viewportSize = Vector3.createNew(320, 240, 0);

    /**
     * Der Zielvektor (look-at)
     */
    @NotNull
    private Vector3 target = Vector3.createNew();

    /**
     * Defines if depth testing is enabled for this camera or not.
     */
    private boolean hasDepthTest = true;

    /**
	 * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
     * @see #CameraNode
	 */
	public CameraNode() {}

    /**
     * Minimaler (kleinstes) und Maximaler (größtes) Bildwinkel (Fov) der OpenGL Kamera
     */
    private float minFovy = 10;
    private float maxFovy = 80;


	/**
	 * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
	 * @param frustum Der Sichtkegel
	 */
	public CameraNode(@NotNull Frustum frustum) {
		this.frustum = frustum;
	}

	/**
	 * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
	 *
	 * @param position Die Position der Kamera
	 * @param lookAt Der Punkt, auf den die Kamera blickt
	 * @param up Der Hoch-Vektor der Kamera
	 */
	public CameraNode(@NotNull Vector3 position, @NotNull Vector3 lookAt, @NotNull Vector3 up) {
		define(position, lookAt, up);
	}

	/**
	 * Initialisiert eine neue Instanz der {@see CameraNode}-Klasse
	 *
	 * @param positionX X-Komponente des Positionsvektors
	 * @param positionY Y-Komponente des Positionsvektors
	 * @param positionZ Z-Komponente des Positionsvektors
	 * @param eyeX X-Komponente des Sichtvektors
	 * @param eyeY Y-Komponente des Sichtvektors
	 * @param eyeZ Z-Komponente des Sichtvektors
	 * @param upX X-Komponente des Hochvektors
	 * @param upY Y-Komponente des Hochvektors
	 * @param upZ Z-Komponente des Hochvektors
     * @see <a href="http://assets.stupeflix.com/code/images/cartesian.png">coordinate system</a>
	 */
	public CameraNode(float positionX, float positionY, float positionZ, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ) {
		define( positionX, positionY, positionZ,
				eyeX, eyeY, eyeZ,
				upX, upY, upZ);
	}

    /**
     * Setzt den Viewport
     * @param x X-Koordinate in px
     * @param y Y-Koordinate in px
     * @param width Breite in px
     * @param height Höhe in px
     */
    public void setViewport(float x, float y, float width, float height) {
        viewportCoords.set(x, y, 0);
        viewportSize.set(width, height, 0);
        // TODO: FUCK YOU 4/3 Aspect!!!!! Wir haben doch 800/480 = ~16:10 (1.6666)
        frustum.setAspectRatio(width/height);
        Utils.log(TAG, "[width:" + width + "|height:" + height + "|aspect:" + frustum.getAspectRatio() + "]");
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
        return viewportSize;
    }

    /**
     * Aspektratio ermitteln
     * @return
     */
    public float getAspectRatio() {
        return frustum.getAspectRatio();
    }

    /**
     * Liefert den Winkel des horizontalen Sichtbereiches unter Beachtung des Zoomfaktors
     * @return Der Winkel in Radians
     */
    public float getHorizontalFieldOfViewEffective() {
        return frustum.getHorizontalFieldOfViewEffective();
    }

	/**
	 * Bezieht den Sichtkegel
	 * @return
	 */
	@NotNull
	public Frustum getFrustum() {
		return frustum;
	}

	/**
	 * Setzt den Sichtkegel
	 * @param frustum Der Kegel
	 */
	@Deprecated
	public void setFrustum(@NotNull Frustum frustum) {
		this.frustum = frustum;
        setPosition(frustum.getCameraPosition()); // TODO: Referenz verwenden
	}

	/**
	 * Setzt die Kameraparameter
	 * @param position Die Position der Kamera
	 * @param lookAt Der Punkt, auf den die Kamera blickt
	 * @param up Der Hoch-Vektor der Kamera
	 * @see #define(float, float, float, float, float, float, float, float, float) 
	 */
	public void define(@NotNull Vector3 position, @NotNull Vector3 lookAt, @NotNull Vector3 up) {
		frustum.setCamera(position, lookAt, up);
        setPosition(frustum.getCameraPosition()); // TODO: Referenz verwenden
	}

    /**
	 * Setzt die Kameraparameter
	 * @param positionX X-Komponente des Positionsvektors
	 * @param positionY Y-Komponente des Positionsvektors
	 * @param positionZ Z-Komponente des Positionsvektors
	 * @param eyeX X-Komponente des Sichtvektors
	 * @param eyeY Y-Komponente des Sichtvektors
	 * @param eyeZ Z-Komponente des Sichtvektors
	 * @param upX X-Komponente des Hochvektors
	 * @param upY Y-Komponente des Hochvektors
	 * @param upZ Z-Komponente des Hochvektors
	 */
	public void define(float positionX, float positionY, float positionZ, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ) {
		Vector3 position = Vector3.createNew(positionX, positionY, positionZ);
		Vector3 eye = Vector3.createNew(eyeX, eyeY, eyeZ);
		Vector3 up = Vector3.createNew(upX, upY, upZ);

		frustum.setCamera(position, eye, up);
        setPosition(frustum.getCameraPosition()); // TODO: Referenz verwenden

		position.recycle();
		eye.recycle();
		up.recycle();
	}

	/**
	 * Liefert den Aufwärtsvektor der Kamera
	 *
	 * @return Der Aufwärtsvektor
	 */
	@NotNull
	public final Vector3 getCameraUpVector() {
		return frustum.getCameraUpVector();
	}

	/**
	 * Liefert den Rechtsvektor der Kamera
	 *
	 * @return Der Rechtsvektor
	 */
	@NotNull
	public final Vector3 getCameraRightVector() {
		return frustum.getCameraRightVector();
	}

	/**
	 * Liefert den Augenvektor der Kamera
	 *
	 * @return Der Augenvektor
	 */
	@NotNull
	public final Vector3 getCameraEyeVector() {
		return frustum.getCameraEyeVector();
	}

	/**
	 * Spezifische Implementierung der Aktualisierungslogik
	 *
	 * @param deltat Zeitdifferenz zum vorherigen Frame
	 */
	@Override
	protected void updateInternal(float deltat) {
		// Nur nötig, wenn Kamera animiert wird. --> Pfade setzen, ...
        frustum.setCamera(getPosition(), getTarget(), getCameraUpVector());

	}

    /**
     * Spezifische Implementierung des Verarbeitungsvorganges
     * @param state Die State-Referenz
     */
    @Override
    protected void processInternal(@NotNull ProcessingState state) {
        GL10 gl = state.getGl();
        assert gl != null;

        gl.glViewport((int) viewportCoords.x, (int) viewportCoords.y, (int) viewportSize.x, (int) viewportSize.y);
        frustum.apply(gl);

        if(hasDepthTest) {
		    gl.glEnable(GL10.GL_DEPTH_TEST);
        } else {
            gl.glDisable(GL10.GL_DEPTH_TEST);
        }
    }


    /**
	 * Spezifische Implementierung des Rendervorganges
	 *
	 * @param gl Die OpenGL-Referenz
	 */
	@Override
	protected void renderInternal(@NotNull GL10 gl) {
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
        frustum.setCamera(getPosition(), point, frustum.getCameraUpVector());
    }

    /**
     * Liefert den Sichtpunkt der Kamera absolut, ohne die Position zu verändern
     *
     * @see CameraNode#setTarget(net.gtamps.android.core.math.Vector3)
     */
    @NotNull
    public Vector3 getTarget() {
        return frustum.getCameraEyeVector(); // TODO: Öhm ... Eye == LookAt == Target? o.O Gedanken sortieren!
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
     * @param offset Der Wert
     * @param moveTarget Gibt an, ob das Ziel auch verschoben werden soll
     */
    public void move(@NotNull Vector3 offset, boolean moveTarget) {
        Vector3 position = getPosition();

        position.addInPlace(offset);
        if (moveTarget) target.addInPlace(offset);

        // Frustum abgleichen
        frustum.setCamera(position, target, frustum.getCameraUpVector());
    }

	/**
	 * Verschiebt die Kamera (und ggf. den Sichtpunkt) relativ um den angegebenen Wert.
	 *
	 * @param offsetX     Wert der X-Bewegung
	 * @param offsetY     Wert der Y-Bewegung
	 * @param offsetZ     Wert der Z-Bewegung
	 * @param moveTarget Gibt an, ob das Ziel auch verschoben werden soll
	 */
	public void move(float offsetX, float offsetY, float offsetZ, boolean moveTarget) {
		Vector3 position = getPosition();
		position.addInPlace(offsetX, offsetY, offsetZ);
		if (moveTarget) target.addInPlace(offsetX, offsetY, offsetZ);
		// Frustum abgleichen
		frustum.setCamera(position, target, frustum.getCameraUpVector());
	}

    /**
     * Setzt den Zoomfaktor und prüft auf "unmögliche/ungewollte Zoomwerte"
     *
     * @param factor Der Faktor
     */
    public void setZoomFactor(float factor) {
        assert factor > 0;
        if (frustum.getHorizontalFieldOfView()/factor >= minFovy && frustum.getHorizontalFieldOfView()/factor <= maxFovy){
            frustum.setHorizontalFieldOfView(frustum.getHorizontalFieldOfView(), factor);
        }
    }

    /**
     * Liefert den Zoomfaktor
     * @return Der Zoomfaktor
     */
    public float getZoomFactor() {
        return frustum.getZoomFactor();
    }

    /**
     * Setzt den horizontalen Blickwinkel
     *
     * @param fovAngleRadians Der Blickwinkel in Radians
     * @param resetZoom Gibt an, ob der Zoom zurückgesetzt werden soll
     */
    public void setFieldOfViewHorizontal(float fovAngleRadians, boolean resetZoom) {
        frustum.setHorizontalFieldOfView(fovAngleRadians, resetZoom);
    }

    /**
     * Setzt den horizontalen Blickwinkel, ohne den Zoom zurückzusetzen
     *
     * @param fovAngleRadians Der Blickwinkel in Radians
     * @see CameraNode#setFieldOfViewHorizontal(float, boolean)
     */
    public void setFieldOfViewHorizontal(float fovAngleRadians) {
        frustum.setHorizontalFieldOfView(fovAngleRadians, false);
    }

    /**
     * Setzt die Rotation um den Sichtvektor absolut
     *
     * @param rotationAngleRadians Die absolute Rotation um den Sichtvektor
     */
    public void setRotationAroundEyeVector(float rotationAngleRadians) {
        Vector3 eyeVector = frustum.getCameraEyeVector().sub(getPosition()); // TODO: ist eye korrekt? Und die Richtung?
        Matrix4 rotationMatrix = Matrix4.getRotationAxisAngle(eyeVector, rotationAngleRadians);
        rotationMatrix.transform(eyeVector);
        rotationMatrix.recycle();
    }

    @Override
    protected void afterProcess(@NotNull ProcessingState state) {

    }

    /**
     * Enables Depth Testing for this camera. Default is <code>true</code>.
     * @param isEnabled
     */
    public void enableDepthTest(boolean isEnabled) {
        hasDepthTest = isEnabled;
    }

    public void moveXY(Vector3 position) {
        define(position.x, position.y,getPosition().z,position.x, position.y,getTarget().z,getCameraUpVector().x,getCameraUpVector().y,getCameraUpVector().z);
    }
}
