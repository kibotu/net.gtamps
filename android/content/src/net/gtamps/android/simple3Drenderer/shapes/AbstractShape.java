package net.gtamps.android.simple3Drenderer.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.simple3Drenderer.StaticTextureHolder;

public abstract class AbstractShape {
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer texBuffer;
	protected float[] texCoords;

	protected String texture;
	AbstractShape(String texture){
		this.texture = texture;
		
		texCoords = StaticTextureHolder.getTextureCoordinates(texture);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.position(0);
	}
	
	public void bindTexture(GL10 gl){
		gl.glBindTexture(GL10.GL_TEXTURE_2D, StaticTextureHolder.getTextureID(this.texture, gl));
	}

	public abstract void draw(GL10 gl);
}
