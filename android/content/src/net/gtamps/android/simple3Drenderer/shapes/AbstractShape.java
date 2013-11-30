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
	protected int textureID = -1;

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
		if(textureID==-1){
			textureID = StaticTextureHolder.getTextureID(this.texture, gl);
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
	}

	public abstract void draw(GL10 gl);
}
