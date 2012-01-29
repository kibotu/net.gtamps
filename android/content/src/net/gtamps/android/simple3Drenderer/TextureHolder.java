package net.gtamps.android.simple3Drenderer;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;


public class TextureHolder {
	
	private static final int MAX_TEXTURES = 24;
	private static int[] textureIDs = new int[MAX_TEXTURES];
	private static int nextTextureID = 1;
	private static HashMap<Bitmap, Integer> textureLookup = new HashMap<Bitmap, Integer>();
	private static boolean firstStart = true;
	
	private static Integer lookupId = null;
	public static int getTextureID(Bitmap texture, GL10 gl){
		lookupId = textureLookup.get(texture);
		if(lookupId==null){
			if(firstStart){
				gl.glGenTextures(MAX_TEXTURES, textureIDs, 0);
				firstStart = false;
			}

			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[nextTextureID]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);
			textureLookup.put(texture, nextTextureID);
			
			nextTextureID++;
			lookupId = nextTextureID-1;
		}
		return textureIDs[lookupId];
	}
	
	
}
