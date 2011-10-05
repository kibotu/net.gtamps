package net.gtamps.android.core.graph;

import net.gtamps.android.core.renderer.Vbo;
import net.gtamps.android.core.utils.OpenGLUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Basisknoten für einfache VBO-Elemente
 */
public abstract class VboBaseNode extends RenderableNode {

	/**
	 * Die Textur-ID.
	 * <p>Gültige Werte liegen in Bereich 0..n</p>
	 */
	private int textureId;

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

    protected int u;
    protected int v;


    /**
     * Defines if MipMapping is enabled.
     */
    protected boolean hasMipMap;

    public VboBaseNode() {
        reset();
    }

	/**
	 * Setzt den Knoten zurück
	 */
	public final void reset() {
		textureId = -1;
		textureBufferId = -1;
		textureBufferOffsetId = -1;
        u = 0;
        v = 0;
	}

	/**
	 * fps expensive preferably called only once at start
	 *
	 * @param gl
	 */
	public final void bindTexture(@NotNull final GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
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

	/**
	 * Liefert die Textur-ID
	 *
	 * @return Die Textur-ID
	 */
	public final int getTextureId() {
		return textureId;
	}

	/**
	 * Liefert die Textur-Puffer-ID
	 *
	 * @return Die Puffer-ID
	 */
	public final int getTextureBufferId() {
		return textureBufferId == -1 ? getVbo().textureCoordinateBufferId : textureBufferId;
	}

	/**
	 * Leifert den Offset im Texturpuffer
	 *
	 * @return Der Offset
	 */
	public final int getTextureBufferOffsetId() {
		return textureBufferOffsetId == -1 ? 0 : textureBufferOffsetId;
	}

	/**
	 * Setzt die VBO-Instanz
	 *
	 * @param vbo Die Instanz
	 */
	protected abstract void setVbo(@Nullable Vbo vbo);

	/**
	 * Liefert die VBO-Instanz
	 *
	 * @return Die VBO-Instanz
	 */
	@Nullable
	protected abstract Vbo getVbo();

	/**
	 * Allocates a new array of vertices for a 3d object.
	 *
	 * @return vertices
	 */
	protected abstract float[] getDefaultVertices();

	/**
	 * Allocates a new array of normal vectors for a 3d object.
	 *
	 * @return normal array
	 */
	protected abstract float[] getDefaultNormals() ;

	/**
	 * Allocates a new array of indices for a 3d object.
	 *
	 * @return indices array
	 */
	protected abstract short[] getDefaultIndices();

	/**
	 * Allocates a new array of texture coordinates for a 3d object.
	 *
	 * @return texture coordinate array
	 */
	protected abstract float[] getDefaultTextureCoordinates();
	/**
	 * Allocates a new array of color material for a 3d object.
	 *
	 * @param r Rot-Komponente des ambienten Lichts (0..1)
	 * @param g Grün-Komponente des ambienten Lichts (0..1)
	 * @param b Blau-Komponente des ambienten Lichts (0..1)
	 * @return color material array
	 */
	protected abstract float[] getDefaultColorMaterial(final float r, final float g, final float b);

    /**
     * Enables MipMapping. Has no effect if OpenGL < GL11 or texture is not a MipMap.Default is <code>true</code>.
     * @param hasMipMap
     */
    public void enableMipMap(boolean hasMipMap) {
        this.hasMipMap = hasMipMap;
    }
}
