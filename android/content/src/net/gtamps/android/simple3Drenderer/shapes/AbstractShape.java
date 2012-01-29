package net.gtamps.android.simple3Drenderer.shapes;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

import net.gtamps.android.simple3Drenderer.TextureHolder;

public abstract class AbstractShape {
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer texBuffer;

	protected Bitmap texture;
	AbstractShape(Bitmap texture){
		this.texture = texture;
	}
	
	public void bindTexture(GL10 gl){
		gl.glBindTexture(GL10.GL_TEXTURE_2D, TextureHolder.getTextureID(this.texture, gl));
	}

	public abstract void draw(GL10 gl);
}
