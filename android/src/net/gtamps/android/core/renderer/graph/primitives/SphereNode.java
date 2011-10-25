package net.gtamps.android.core.renderer.graph.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Szenenknoten, der eine Kugel repräsentiert
 */
public class SphereNode extends RenderableNode {

	/**
	 * Der Radius der Kugel
	 */
	private final float radius;

	/**
	 * The number of subdivisions around the Z axis (similar to lines of longitude).
	 */
	private final int slices;

	/**
     * The number of subdivisions along the Z axis (similar to lines of latitude).
	 */
	private final int stacks;

    	/**
	 * Die Textur-ID.
	 * <p>Gültige Werte liegen in Bereich 0..n</p>
	 */
	protected int textureId;

	/**
	 * Die Texturpuffer-ID
	 * <p>Gültige Werte liegen in Bereich 0..n</p>
	 */
	private int textureBufferId;

	/**
	 * Der Offset im Texturpuffer
	 * <p>Gültige Werte liegen in Bereich 0..n</p>
	 */
	private int textureBufferOffsetId;

	/**
	 * Initialisiert eine neue Instanz des SphereNodes
	 * @param radius Der Radius
	 * @param slices Die Anzahl der Slices (TODO: klären)
	 * @param stacks Die Anzahl der Stacks (TODO: klären)
	 */
	public SphereNode(float radius, int slices, int stacks) {
		assert radius > 0;
		assert slices >= 1;
		assert stacks >= 1;

		this.radius = radius;
		this.slices = slices;
		this.stacks = stacks;
        reset();
    }

	/**
	 * Setzt den Knoten zurück
	 */
	public final void reset() {
		textureId = -1;
		textureBufferId = -1;
		textureBufferOffsetId = -1;
	}

	/**
	 * Spezifische Implementierung des Rendervorganges
	 *
	 * @param gl Die OpenGL-Referenz
	 */
	@Override
	protected void renderInternal(@NotNull GL10 gl) {

		// OpenGL 1.1-Instanz beziehen
		final GL11 gl11 = (GL11) gl;

         // bind texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        gl11.glEnable(GL11.GL_TEXTURE_2D);
        gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		// alpha blending
		gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		gl11.glEnable(GL11.GL_BLEND);

		// kill alpha fragments
		gl11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		gl11.glEnable(GL11.GL_ALPHA_TEST);

		// ... und ab.
		GLUT.glutSolidSphere(gl11, radius, slices, stacks);

        gl11.glDisable(GL11.GL_TEXTURE_2D);
        gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	/**
	 * Spezifische Implementierung der Aktualisierungslogik
	 *
	 * @param deltat Zeitdifferenz zum vorherigen Frame
	 */
	@Override
	protected void updateInternal(float deltat) {
	}

	/**
	 * Spezifische Implementierung des Bereinigungsvorganges
	 *
	 * @param state Die State-Referenz
	 */
	@Override
	protected void cleanupInternal(@NotNull ProcessingState state) {
	}

	/**
	 * Spezifische Implementierung des Setupvorganges
	 *
	 * @param state Die State-Referenz
	 */
	@Override
	protected void setupInternal(@NotNull ProcessingState state) {
	}

	/**
	 * Setzt die Textur-ID
	 *
	 * @param textureId
	 */
	public final void setTextureId(final int textureId) {
		this.textureId = textureId;
	}

	/**
	 * Setzt die Textur-Puffer-ID
	 *
	 * @param textureBufferId
	 */
	public final void setTextureBufferId(final int textureBufferId) {
		this.textureBufferId = textureBufferId;
	}

	/**
	 * Setzt den Offset im Texturpuffer
	 *
	 * @param textureBufferOffsetId
	 */
	public final void setTextureBufferOffsetId(final int textureBufferOffsetId) {
		this.textureBufferOffsetId = textureBufferOffsetId;
	}
}
