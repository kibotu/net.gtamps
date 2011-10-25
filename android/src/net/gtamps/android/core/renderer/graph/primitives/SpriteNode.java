package net.gtamps.android.core.renderer.graph.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.VboBaseNode;
import net.gtamps.android.core.renderer.mesh.buffermanager.Vbo;
import net.gtamps.android.core.utils.OpenGLUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Scenenknoten, der einen Sprite repräsentiert
 */
public class SpriteNode extends VboBaseNode {

    /**
	 * Die zu verwendende Vertexbuffer-Instanz
	 */
	@Nullable
	private static Vbo vbo;

    /**
     * Setzt die Ausdehnung des Sprite-Knotens als Basis für das Scaling
     * @param width Die Breite
     * @param height Die Höhe
     * @see #setScaling(net.gtamps.shared.math.Vector3)
     * @see #setScaling(float, float, float)
     */
    public void setDimension(float width, float height) {
        setDimension(width, height, 0);
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

		// Die VBO-Instanz beziehen
		Vbo vbo = getVbo();
		assert vbo != null;

		// enable texture
		gl11.glEnable(GL10.GL_TEXTURE_2D);
		gl11.glEnable(GL11.GL_COLOR_MATERIAL);

		// set active texture
//		gl.glActiveTexture(_currentTextureId);
		bindTexture(gl11);

		gl11.glEnable(GL11.GL_BLEND);

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
//		gl11.glNormalPointer(GL11.GL_FLOAT,0,0);
		gl11.glNormalPointer(GL11.GL_FIXED, 0, 0);

		// bind texture vbo id
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, getTextureBufferId());

		// Zeichne Texturkoordinaten
		gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, getTextureBufferOffsetId());

		// indices
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, vbo.indexBufferId);

		// Zeichne Triangles
		/* drawing styles: GL_POINTS, GL_LINES, GL_LINE_STRIP, GL_LINE_LOOP
				 * GL_TRIANGLES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN */
//		assert vbo.indicesSize > 0;
		gl11.glDrawElements(drawingStyle.ordinal(), vbo.indexBuffer.capacity(), GL11.GL_UNSIGNED_SHORT, 0);

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

		// OpenGL 1.1-Instanz beziehen
		//final GL11 gl11 = state.getGl11();

		// Puffer aufräumen
		//gl11.glDeleteBuffers(2, new int [] { _vertexVboId, _indexVboId,}, 0);
	}

	/**
	 * Ich glaube immer noch, dass das Overkill ist ... ~markus
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SpriteNode)) return false;
		if (!super.equals(o)) return false;

		SpriteNode that = (SpriteNode) o;

		if (getTextureBufferId() != that.getTextureBufferId()) return false;
		if (getTextureBufferOffsetId() != that.getTextureBufferOffsetId()) return false;
		if (getTextureId() != that.getTextureId()) return false;

		return true;
	}

	/**
	 * Ich glaube immer noch, dass das Overkill ist ... ~markus
	 * @return
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + getTextureId();
		result = 31 * result + getTextureBufferId();
		result = 31 * result + getTextureBufferOffsetId();
		return result;
	}

    @Override
	protected float[] getDefaultVertices() {
		final float c = 1f;
		return new float[]{
				c, c, 0.0f, // oben rechts
				-c, c, 0.0f, // oben links
				-c, -c, 0.0f, // unten links
				c, -c, 0.0f  // rechts unten
		};
	}

    @Override
	protected float[] getDefaultNormals() {
		final float c = 1f;
		return new float[]{
				0f, 0f, 1f,
				0f, 0f, 1f,
				0f, 0f, 1f,
				0f, 0f, 1f
		};
	}

    @Override
	protected  short[] getDefaultIndices() {
		return new short[]{
				0, 1, 2, 2, 3, 0
		};
	}

    @Override
	protected float[] getDefaultTextureCoordinates() {
		return new float[]{
				1f, 0f, // oben rechts
				0f, 0f, // oben links
				0f, 1f, // unten links
				1f, 1f  // unten rechts
		};
	}


    @Override
	 protected float[] getDefaultColorMaterial(final float r, final float g, final float b){
		assert r >= 0 && r <= 1;
		assert g >= 0 && g <= 1;
		assert b >= 0 && b <= 1;

		final float a = 1f;
		return new float[]{
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
		};
	}

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {

        if(vbo != null) {
            return;
        }

        float[] vertices = getDefaultVertices();
		short[] indices = getDefaultIndices();
		float[] normals = getDefaultNormals();

        FloatBuffer vertexBuffer = OpenGLUtils.toFloatBufferPositionZero(vertices);
		ShortBuffer indexBuffer = OpenGLUtils.toShortBuffer(indices);
		FloatBuffer normalBuffer = OpenGLUtils.toFloatBufferPositionZero(normals);

        float[] color = getDefaultColorMaterial(getAmbientLightRed(), getAmbientLightGreen(), getAmbientLightBlue());
		FloatBuffer colorBuffer = null;
        if(color.length > 1) {
            colorBuffer = OpenGLUtils.toFloatBufferPositionZero(color);
        }
        float[] texturecCoords = getDefaultTextureCoordinates();
        FloatBuffer textureBuffer = null;
        if(texturecCoords.length > 1) {
           textureBuffer = OpenGLUtils.toFloatBufferPositionZero(texturecCoords);
        }

        vbo = new Vbo(vertexBuffer,indexBuffer,normalBuffer,colorBuffer,textureBuffer);

        vbo.alloc(state.getGl());
        vbo.clearBuffer();
    }

    @Override
    protected void setVbo(@Nullable Vbo vbo) {
       this.vbo = vbo;
    }

    @Override
    protected Vbo getVbo() {
        return vbo;
    }
}
