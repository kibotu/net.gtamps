package net.gtamps.android.simple3Drenderer.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/*
 * A cube with texture. 
 * Define the vertices for only one representative face.
 * Render the cube by translating and rotating the face.
 */
public class TexturedCube extends AbstractShape {

	private float[] vertices = { // Vertices for a face
	-1.0f, -1.0f, 0.0f, // 0. left-bottom-front
			1.0f, -1.0f, 0.0f, // 1. right-bottom-front
			-1.0f, 1.0f, 0.0f, // 2. left-top-front
			1.0f, 1.0f, 0.0f // 3. right-top-front
	};

	float[] texCoords = { // Texture coords for the above face (NEW)
	0.0f, 1.0f, // A. left-bottom (NEW)
			1.0f, 1.0f, // B. right-bottom (NEW)
			0.0f, 0.0f, // C. left-top (NEW)
			1.0f, 0.0f // D. right-top (NEW)
	};

	// Constructor - Set up the buffers
	public TexturedCube(Bitmap bitmap) {
		super(bitmap);
		// Setup vertex-array buffer. Vertices in float. An float has 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		vertexBuffer.put(vertices); // Copy data into buffer
		vertexBuffer.position(0); // Rewind

		// Setup texture-coords-array buffer, in float. An float has 4 bytes
		// (NEW)
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.position(0);
	}

	// Draw the shape
	public void draw(GL10 gl) {
		bindTexture(gl);
		gl.glFrontFace(GL10.GL_CCW); // Front face in counter-clockwise
										// orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK); // Cull the back face (don't display)

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // Enable
																// texture-coords-array
																// (NEW)
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define
																// texture-coords
																// buffer (NEW)

		// front
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// left
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// back
		gl.glPushMatrix();
		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// right
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// top
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// bottom
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // Disable
																// texture-coords-array
																// (NEW)
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

}
