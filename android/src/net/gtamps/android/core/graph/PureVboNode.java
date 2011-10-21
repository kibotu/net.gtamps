package net.gtamps.android.core.graph;

import net.gtamps.android.core.renderer.Vbo;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Ein Knoten, der ausschließlich mittels VBO betrieben wird
 */
public class PureVboNode extends VboBaseNode {

	/**
	 * Die VBO-Instanz
	 */
	@NotNull
	private Vbo vbo;

	/**
	 * Erzeugt eine neue Instanz des VBO-Knotens
	 * @param vbo Die VBO-Instanz
	 */
	public PureVboNode(@NotNull Vbo vbo) {
		this.vbo = vbo;
	}

    public PureVboNode() {
    }

	/**
	 * Setzt die VBO-Instanz
	 *
	 * @param vbo Die Instanz
	 */
	@Override
	protected void setVbo(@NotNull Vbo vbo) {
		this.vbo = vbo;
	}

	/**
	 * Liefert die VBO-Instanz
	 *
	 * @return Die VBO-Instanz
	 */
	@Override
	@NotNull
    public Vbo getVbo() {
		return vbo;
	}

    @Override
    protected float[] getDefaultVertices() {
        return new float[0];
    }

    @Override
    protected float[] getDefaultNormals() {
        return new float[0];
    }

    @Override
    protected short[] getDefaultIndices() {
        return new short[0];
    }

    @Override
    protected float[] getDefaultTextureCoordinates() {
        return new float[0];
    }

    @Override
    protected float[] getDefaultColorMaterial(float r, float g, float b) {
        return new float[0];
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

		// enable texture
        if(texturesEnabled) {
            gl11.glEnable(GL10.GL_TEXTURE_2D);
            if(hasMipMap && gl instanceof GL11) {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
            } else {
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            }
        }

        gl11.glDisable(GL11.GL_COLOR_MATERIAL);

		// set active texture
//		gl.glActiveTexture(_currentTextureId);
		bindTexture(gl11);

		// alpha blending
		gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		gl11.glEnable(GL11.GL_BLEND);

		// kill alpha fragments
		gl11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		gl11.glEnable(GL11.GL_ALPHA_TEST);

		// Vertex Array-State aktivieren und Vertexttyp setzen
		gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		gl11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		gl11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		// Binde Color Material Buffer
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo.colorBufferId);

		// Zeichne Color Material
		gl11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);

		// Vertex binden
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo.vertexBufferId);

		// Zeichne Vertices!
		gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		// binde normal vbo id
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo.normalBufferId);

		// zeichne normals
		gl11.glNormalPointer(GL11.GL_FLOAT,0,0);

		// bind texture vbo id
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, getTextureBufferId());

		// Zeichne Texturkoordinaten
		gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, getTextureBufferOffsetId());

		// indices
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, vbo.indexBufferId);

		// Binde Color Material Buffer
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo.colorBufferId);

		// Zeichne Color Material
		gl11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);

		// Zeichne Triangles
		gl11.glDrawElements(drawingStyle.ordinal(), vbo.indexBuffer.capacity(), GL11.GL_UNSIGNED_SHORT, 0);

        // texture offset for repeating textures
        if (u != 0 || v != 0) {
            gl.glMatrixMode(GL10.GL_TEXTURE);
            gl.glLoadIdentity();
            gl.glTranslatef(u, v, 0);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
        }

		// disable texture
		gl11.glDisable(GL11.GL_TEXTURE_2D);

		// disable blending
		gl.glDisable(GL11.GL_BLEND);
		gl.glDisable(GL11.GL_ALPHA_TEST);
		gl.glDisable(GL11.GL_COLOR_MATERIAL);

		// Vertex Array-State deaktivieren
		gl11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		gl11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		gl11.glDisableClientState(GL11.GL_COLOR_ARRAY);

		// Puffer abw�hlen
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
		// TODO: Cleanup des VBO-Knotens
	}

	/**
	 * Spezifische Implementierung des Setupvorganges
	 *
	 * @param state Die State-Referenz
	 */
	@Override
	protected void setupInternal(@NotNull ProcessingState state) {
        vbo.alloc(state.getGl());
        vbo.clearBuffer();
	}
}
