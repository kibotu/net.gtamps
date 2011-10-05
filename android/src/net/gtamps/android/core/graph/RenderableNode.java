package net.gtamps.android.core.graph;

import android.view.ViewDebug;
import net.gtamps.android.core.math.Vector3;
import net.gtamps.android.core.utils.OpenGLUtils;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.security.PrivateKey;

/**
 * Basisklasse für renderbare Szenenknoten
 * 
 * Dies ist die Basisklasse eines rendert Szenenknoten. Sie übernimmt das Management
 * der Sichtbarkeit, der Orientierung und des Dispatchings von Funktionsaufrufen
 * an Kindelemente. Spezifische Szenenknoten leiten von dieser Klasse ab und
 * implementieren eine eigenständige Update- und Renderlogik in den überladenen
 * Funktionen.
 * @author sunside
 */
public abstract class RenderableNode extends SceneNode {

    private static final String TAG = RenderableNode.class.getSimpleName();

	/**
	 * Die ambiente Beleuchtung dieses Knotens
	 */
	private Vector3 ambientLight = Vector3.createNew(1.0f, 1.0f, 1.0f);

    protected OpenGLUtils.DrawingStyle drawingStyle = OpenGLUtils.DrawingStyle.GL_TRIANGLES;

	protected boolean vertexColorsEnabled = true;
	protected boolean doubleSidedEnabled = false;
	protected boolean texturesEnabled = true;
	protected boolean normalsEnabled = true;
	protected boolean ignoreFaces = false;
	protected boolean colorMaterialEnabled = false;
	protected boolean lightingEnabled = true;

    protected OpenGLUtils.ShadeStyle shadingStyle = OpenGLUtils.ShadeStyle.SMOOTH;
	protected float pointSize = 3f;
	protected boolean pointSmoothing = true;
	protected float lineWidth = 1f;
	protected boolean lineSmoothing = false;

	/**
	 * Spezifische Implementierung des Verarbeitungsvorganges
	 * @param state Die State-Referenz
	 */
	protected void processInternal(@NotNull ProcessingState state) {
		GL10 gl = state.getGl();
		assert gl != null;

        // Model view aktivieren
        gl.glPushMatrix();
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        // Objekt verschieben
        Vector3 position = getPosition();
        gl.glTranslatef(position.x, position.y, position.z);

        // Object Rotieren (Roll - Pitch - Yaw)
        Vector3 rotation = getRotation();
        gl.glRotatef(rotation.x, 1, 0, 0);
        gl.glRotatef(rotation.y, 0, 1, 0);
        gl.glRotatef(rotation.z, 0, 0, 1);

        // Object skalieren
        Vector3 scaling = getScaling();
        Vector3 dimension = getDimension();
        gl.glPushMatrix();
        gl.glScalef(dimension.x * scaling.x, dimension.y * scaling.y, dimension.z * scaling.z);
//        Log.e(TAG,"" + dimension + " " + scaling);

        // Ambiente Farbe setzen
        Vector3 color = getAmbientLight();
        gl.glColor4f(color.x, color.y, color.z, 1f);

//        gl.glLoadMatrixf(getCombinedTransformation());

		renderInternal(gl);
        gl.glPopMatrix();
	}

	/**
	 * Spezifische Implementierung des Rendervorganges
	 *
	 * @param gl Die OpenGL-Referenz
	 */
	protected abstract void renderInternal(@NotNull GL10 gl);

    @Override
    protected void afterProcess(@NotNull ProcessingState state) {
        GL10 gl = state.getGl();
		assert gl != null;
        gl.glPopMatrix();
    }

    /**
	 * Setzt die ambiente Beleuchtung für dieses Objekt
	 * @param r Rot-Komponente 0..1
	 * @param g Grün-Komponente 0..1
	 * @param b Blau-Komponente 0..1
	 */
	public final void setAmbientLight(float r, float g, float b) {
		assert r >= 0 && r <= 1;
		assert g >= 0 && g <= 1;
		assert b >= 0 && b <= 1;

		ambientLight.set(r, g, b);
	}

    /**
     * Setzt das ambiente Licht (die Farbe)
     *
     * @param color Die Farbe
     */
    public void setAmbientLight(@NotNull Vector3 color) {
        assert 0 >= color.x && color.x <= 1;
        assert 0 >= color.y && color.y <= 1;
        assert 0 >= color.z && color.z <= 1;

        ambientLight.set(color);
    }

    /**
	 * Bezieht den Rotwert des ambienten Lichtes
	 * @return Der Wert (0..1)
	 */
	public final float getAmbientLightRed() {
		return ambientLight.x;
	}

	/**
	 * Bezieht den Grünwert des ambienten Lichtes
	 *
	 * @return Der Wert (0..1)
	 */
	public final float getAmbientLightGreen() {
		return ambientLight.y;
	}

	/**
	 * Bezieht den Rotwert des ambienten Lichtes
	 *
	 * @return Der Wert (0..1)
	 */
	public final float getAmbientLightBlue() {
		return ambientLight.z;
	}

    /**
     * Das ambiente Licht (die Farbe)
     * @return Referenz auf die Farbinstanz
     */
    @NotNull
    public Vector3 getAmbientLight() {
        return ambientLight;
    }

    /**
     * Changes the drawing style of the object. Default is <code>GL_TRIANGLES</code>.
     *
     * @param drawingStyle
     */
    public void setDrawingStyle(OpenGLUtils.DrawingStyle drawingStyle) {
        this.drawingStyle = drawingStyle;
    }

    /**
     * Changes the shader for this object. Default is <code>GL_SMOOTH</code>
     *
     * @param shadingStyle
     */
    public void setShadingStyle(OpenGLUtils.ShadeStyle shadingStyle) {
        this.shadingStyle = shadingStyle;
    }

    public void enableVertexColors(boolean isEnabled) {
        vertexColorsEnabled = isEnabled;
    }

    public void enableDoubleSided(boolean isEnabled) {
        doubleSidedEnabled = isEnabled;
    }

    public void enableTextures(boolean isEnabled) {
        texturesEnabled = isEnabled;
    }

    public void enableNormals(boolean isEnabled) {
        normalsEnabled = isEnabled;
    }

    public void ignoreFaces(boolean isIgnored) {
        ignoreFaces = isIgnored;
    }

    public void enableColorMaterial(boolean isEnabled) {
        colorMaterialEnabled = isEnabled;
    }

    public void enableLight(boolean isEnabled) {
        lightingEnabled = isEnabled;
    }

    public void setPointSize(float pointSize) {
        this.pointSize = pointSize;
    }

    public void enablePointSmoothing(boolean isEnabled) {
        pointSmoothing = isEnabled;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void enableLineSmoothing(boolean isEnabled) {
        lineSmoothing = isEnabled;
    }
}
