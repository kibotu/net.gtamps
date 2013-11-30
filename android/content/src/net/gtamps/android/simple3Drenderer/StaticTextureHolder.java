package net.gtamps.android.simple3Drenderer;

import java.util.HashMap;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.simple3Drenderer.textures.TextureMapper;
import net.gtamps.shared.Utils.Logger;
import android.opengl.GLUtils;

public class StaticTextureHolder {

	private static final int MAX_TEXTURES = 24;
	private static int[] textureIDs = new int[MAX_TEXTURES];
	private static int nextTextureID = 1;
	private static HashMap<TextureMapper, Integer> textureLookup = new HashMap<TextureMapper, Integer>();
	private static boolean firstStart = true;
	private static LinkedList<TextureMapper> textureMappers = new LinkedList<TextureMapper>();
//	private static Integer lookupId = null;

	public static int getTextureID(String texture, GL10 gl) {
		return textureIDs[checkIfLoaded(texture, gl)];
	}

	private static int checkIfLoaded(String texture, GL10 gl) {
		for (TextureMapper tm : textureMappers) {
			if (tm.has(texture)) {
				if (!tm.isLoaded())
					addTexture(gl, tm);
				return textureLookup.get(tm);
			}
		}

		// default
		return 0;
	}

//	private static TextureMapper getTextureByName(String texture) {
//		for (TextureMapper tm : textureMappers) {
//			if (tm.has(texture)) {
//				return tm;
//			}
//		}
//		return null;
//	}

	public static float[] getTextureCoordinates(String texture) {

		for (TextureMapper tm : textureMappers) {
			if (tm.has(texture)) {
				return tm.getCoords(texture);
			}
		}
		return new float[] { 0.0f, 1.0f, // A. left-bottom (NEW)
				1.0f, 1.0f, // B. right-bottom (NEW)
				0.0f, 0.0f, // C. left-top (NEW)
				1.0f, 0.0f // D. right-top (NEW)
		};
	}

	public static void add(TextureMapper createdTileMap) {
		textureMappers.add(createdTileMap);
	}

	private static void addTexture(GL10 gl, TextureMapper texture) {
		if (firstStart) {
			gl.glGenTextures(MAX_TEXTURES, textureIDs, 0);
			firstStart = false;
		}
		Logger.i("TextureHolder", "Loading new TextureMap with "+texture.getAmount()+" tiles. TextureID = "+textureIDs[nextTextureID]);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[nextTextureID]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture.getBitmap(), 0);
		textureLookup.put(texture, nextTextureID);
		texture.setLoaded(true);
		nextTextureID++;
	}
}
