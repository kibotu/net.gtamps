package net.gtamps.android.simple3Drenderer.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

public class TexturedQuad extends AbstractShape{

	protected float[] vertices = { // Vertices for a face
	-1.0f, -1.0f, 0.0f, // 0. left-bottom-front
			1.0f, -1.0f, 0.0f, // 1. right-bottom-front
			-1.0f, 1.0f, 0.0f, // 2. left-top-front
			1.0f, 1.0f, 0.0f // 3. right-top-front
	};
	private float scalex;
	private float scaley;


	public TexturedQuad(String texture) {
		this(texture,1.0f,1.0f);
	}

	public TexturedQuad(String texture, float scalex, float scaley) {
		super(texture);
		this.scalex = scalex;
		this.scaley = scaley;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		vertexBuffer.put(vertices); // Copy data into buffer
		vertexBuffer.position(0); // Rewind
	}

	public void draw(GL10 gl) {
		bindTexture(gl);
		gl.glPushMatrix();
		gl.glScalef(scalex, scaley, 1.0f);
		
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
		
		gl.glPopMatrix();
	}
}
