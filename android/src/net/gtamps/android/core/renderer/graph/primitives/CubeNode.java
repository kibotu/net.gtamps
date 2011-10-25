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
 * Scenenknoten, der einen Würfel repräsentiert
 */
public class CubeNode extends VboBaseNode {

    private static final String TAG = CubeNode.class.getSimpleName();

    /**
	 * Die zu verwendende Vertexbuffer-Instanz
	 */
	@Nullable
	private static Vbo vbo;

    public CubeNode() {
        super();
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
		gl11.glEnable(GL10.GL_TEXTURE_2D);

        if(hasMipMap && gl instanceof GL11) {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		} else {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		}

		gl11.glEnable(GL11.GL_COLOR_MATERIAL);

		// set active texture
		// gl.glActiveTexture(_currentTextureId);
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
		// gl11.glNormalPointer(GL11.GL_FLOAT,0,0);
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
		gl11.glDrawElements(GL10.GL_TRIANGLES, vbo.indexBuffer.capacity(), GL11.GL_UNSIGNED_SHORT, 0);

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
	 * Spezifische Implementierung des Setupvorganges
	 *
	 * @param state Die State-Referenz
	 */
	@Override
	protected void setupInternal(@NotNull ProcessingState state) {

         if(vbo != null) {
            return;
        }

        float[] vertices = getDefaultVertices();
        FloatBuffer vertexBuffer = OpenGLUtils.toFloatBufferPositionZero(vertices);

		short[] indices = getDefaultIndices();
		ShortBuffer indexBuffer = OpenGLUtils.toShortBuffer(indices);

		float[] normals = getDefaultNormals();
		FloatBuffer normalBuffer = OpenGLUtils.toFloatBufferPositionZero(normals);

        float[] color = getDefaultColorMaterial(getAmbientLightRed(), getAmbientLightGreen(), getAmbientLightBlue());
		FloatBuffer colorBuffer = OpenGLUtils.toFloatBufferPositionZero(color);

        float[] texturecCoords = getDefaultTextureCoordinates();
        FloatBuffer textureBuffer = OpenGLUtils.toFloatBufferPositionZero(texturecCoords);

        vbo = new Vbo(vertexBuffer,indexBuffer,normalBuffer,colorBuffer,textureBuffer);

        vbo.alloc(state.getGl());
        vbo.clearBuffer();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CubeNode)) return false;
		if (!super.equals(o)) return false;

		CubeNode cubeNode = (CubeNode) o;

		if (getTextureBufferId() != cubeNode.getTextureBufferId()) return false;
		if (getTextureBufferOffsetId() != cubeNode.getTextureBufferOffsetId()) return false;
		if (getTextureId() != cubeNode.getTextureId()) return false;

		return true;
	}

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
              // f  1//2  7//2  5//2
              -c, -c, -c,
              c, c, -c,
              c, -c, -c,
              // f  1//2  3//2  7//2
              -c, -c, -c,
              -c, c, -c,
              c, c, -c,
              // f  1//6  4//6  3//6
              -c, -c, -c,
              -c, c, c,
              -c, c, -c,
              // f  1//6  2//6  4//6
              -c, -c, -c,
              -c, -c, c,
              -c, c, c,
              // f  3//3  8//3  7//3
              -c, c, -c,
              c, c, c,
              c, c, -c,
              // f  3//3  4//3  8//3
              -c, c, -c,
              -c, c, c,
              c, c, c,
              // f  5//5  7//5  8//5
              c, -c, -c,
              c, c, -c,
              c, c, c,
              // f  5//5  8//5  6//5
              c, -c, -c,
              c, c, c,
              c, -c, c,
              // f  1//4  5//4  6//4
              -c, -c, -c,
              c, -c, -c,
              c, -c, c,
              // f  1//4  6//4  2//4
              -c, -c, -c,
              c, -c, c,
              -c, -c, c,
              // f  2//1  6//1  8//1
              -c, -c, c,
              c, -c, c,
              c, c, c,
              // f  2//1  8//1  4//1
              -c, -c, c,
              c, c, c,
              -c, c, c,
        };
	}


    @Override
	protected float[] getDefaultNormals() {
		final float c = 1f;
		return new float[]{
				// f  1//2  7//2  5//2
				0, 0, -c,
				0, 0, -c,
				0, 0, -c,
				// f  c//2  3//2  7//2
				0, 0, -c,
				0, 0, -c,
				0, 0, -c,
				// f  1//6  4//6  3//6
				-c, 0, 0,
				-c, 0, 0,
				-c, 0, 0,
				// f  1//6  2//6  4//6
				-c, 0, 0,
				-c, 0, 0,
				-c, 0, 0,
				// f  3//3  8//3  7//3
				0, c, 0,
				0, c, 0,
				0, c, 0,
				// f  3//3  4//3  8//3
				0, c, 0,
				0, c, 0,
				0, c, 0,
				// f  5//5  7//5  8//5
				c, 0, 0,
				c, 0, 0,
				c, 0, 0,
				// f  5//5  8//5  6//5
				c, 0, 0,
				c, 0, 0,
				c, 0, 0,
				// f  1//4  5//4  6//4
				0, -c, 0,
				0, -c, 0,
				0, -c, 0,
				// f  1//4  6//4  2//4
				0, -c, 0,
				0, -c, 0,
				0, -c, 0,
				// f  2//1  6//1  8//1
				0, 0, c,
				0, 0, c,
				0, 0, c,
				// f  2//1  8//1  4//1
				0, 0, c,
				0, 0, c,
				0, 0, c,
		};
	}

    @Override
	protected short[] getDefaultIndices() {
        return new short[] {
                0, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10, 11,
                12, 13, 14, 15, 16,
                17, 18, 19, 20, 21,
                22, 23, 24, 25, 26,
                27, 28, 29, 30, 31,
                32, 33, 34, 35,
        };
	}

	@Override
	protected float[] getDefaultTextureCoordinates() {
		final float c = 1f;
        return new float[]{
                0f, 0f,
                0f, c,
                c, 0f,
                c, c,

                0f, 0f,
                0f, c,
                c, 0f,
                c, c,

                0f, 0f,
                0f, c,
                c, 0f,
                c, c,

                0f, 0f,
                0f, c,
                c, 0f,
                c, c,

                0f, 0f,
                0f, c,
                c, 0f,
                c, c,

                0f, 0f,
                0f, c,
                c, 0f,
                c, c,
        };
	}

	@Override
	protected float[] getDefaultColorMaterial(final float r, final float g, final float b) {

		assert r >= 0 && r <= 1;
		assert g >= 0 && g <= 1;
		assert b >= 0 && b <= 1;

		final float a = 1f;
		return new float[] {
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,

				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
		};
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
