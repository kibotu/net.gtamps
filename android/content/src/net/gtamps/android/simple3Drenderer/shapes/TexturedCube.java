package net.gtamps.android.simple3Drenderer.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

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
	private float scalex;
	private float scaley;
	private float scalez;

/*= { // Texture coords for the above face (NEW)
	0.0f, 1.0f, // A. left-bottom (NEW)
			1.0f, 1.0f, // B. right-bottom (NEW)
			0.0f, 0.0f, // C. left-top (NEW)
			1.0f, 0.0f // D. right-top (NEW)
	};*/

	// Constructor - Set up the buffers
	public TexturedCube(String bitmap) {
		this(bitmap, 1f,1f,1f);		
	}

	public TexturedCube(String bitmap, float scalex, float scaley, float scalez) {
		super(bitmap);
		this.scalex = scalex;
		this.scaley = scaley;
		this.scalez = scalez;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer(); 
		vertexBuffer.put(vertices);
		vertexBuffer.position(0); 

//		Logger.i(this, "Hello. I'm a Cube. My texture is "+this.texture+". Also, i would like to show you some floats.");
//		for(float f : this.texCoords){
//			Logger.i(this, f);
//		}
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glScalef(scalex, scaley, scalez);
//		bindTexture(gl);
		gl.glFrontFace(GL10.GL_CCW); // Front face in counter-clockwise
										// orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK); // Cull the back face (don't display)

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // Enable
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define

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

		gl.glPopMatrix();
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // Disable
																// texture-coords-array
																// (NEW)
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
		
	}

}
