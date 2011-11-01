package net.gtamps.android.core.renderer.mesh.texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import net.gtamps.android.core.Registry;
import net.gtamps.android.core.utils.TextureCoordinateLoader;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.util.HashMap;

/**
 * This Class loads a texture on-demand.
 */
public class TextureLibrary {

    /**
     * OpenGL pointer is needed in order to load a texture with proper filters and to load it correctly scaled.
     */
    private GL10 gl;

    public HashMap<String, Integer> getTextureResourceIds() {
        return textureResourceIds;
    }

    public HashMap<Integer, BufferedTexture> getTextureCoordinatesResourceIds() {
        return textureCoordinatesResourceIds;
    }

    /**
     * Holds loaded TextureIds. In order to keep track of loaded textures.
     */
    private HashMap<String, Integer> textureResourceIds;

    /**
     * Holds Buffered Texture Coordinates in order to keep track of loaded xml resource ids.
     */
    private HashMap<Integer, BufferedTexture> textureCoordinatesResourceIds;
	private static int atlasId = 0;

    /**
     * Constructs the texture proxy.
     *
     * @param gl
     */
    public TextureLibrary(GL10 gl) {
        this.gl = gl;
        textureResourceIds = new HashMap<String, Integer>(10);
        textureCoordinatesResourceIds = new HashMap<Integer, BufferedTexture>(10);
    }

    public BufferedTexture loadBufferedTexture(int textureResourceId, int textureCoordinateResourceId, boolean generateMipMap) {

        if(textureCoordinatesResourceIds.containsKey(textureCoordinateResourceId)) {
           return textureCoordinatesResourceIds.get(textureCoordinateResourceId);
        }
        BufferedTexture bufferedTexture = TextureCoordinateLoader.loadTextureCoordinates(gl, Registry.getContext(), textureCoordinateResourceId, loadTexture(textureResourceId,generateMipMap));
        textureCoordinatesResourceIds.put(textureCoordinateResourceId, bufferedTexture);

        return bufferedTexture;
    }

    public int loadTexture(final int textureResourceId, boolean generateMipMap) {
        return loadTexture(textureResourceId, generateMipMap, Bitmap.Config.ARGB_8888, false);
    }

    public int loadTexture(final int textureResourceId, boolean generateMipMap, Bitmap.Config bitmapConfig, boolean flipped) {

        if(textureResourceIds.containsKey(textureResourceId)) {
            return textureResourceIds.get(textureResourceId);
        }

        BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
        sBitmapOptions.inPreferredConfig = bitmapConfig;

        Bitmap bitmap;
        if(flipped) {
            // We need to flip the textures vertically
            final Matrix flip = new Matrix();
            flip.postScale(1f, -1f);
             // Generate one texture pointer...
            Bitmap temp = BitmapFactory.decodeResource(Registry.getContext().getResources(), textureResourceId, sBitmapOptions);
            bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
            temp.recycle();
        } else {
            bitmap = BitmapFactory.decodeResource(Registry.getContext().getResources(), textureResourceId, sBitmapOptions);
        }

        int id = alloc(bitmap,generateMipMap);
        textureResourceIds.put(""+textureResourceId,id);

        Logger.i(this, "[w:" + bitmap.getWidth() + "|h:" + bitmap.getHeight() + "|id:" + textureResourceId + "|hasMipMap=" + generateMipMap + "] Bitmap successfully loaded.");

        return id;
    }

    private int alloc(Bitmap bitmap, boolean generateMipMap) {

		int glTextureId = newTextureID(gl);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, glTextureId);

		if(generateMipMap && gl instanceof GL11) {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		} else {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_FALSE);
		}

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

		// 'upload' to gpu
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //Clean up
        bitmap.recycle();

		return glTextureId;
	}

    /**
     * Clear if no textures need to be loaded again.
     */
    public void clear() {
        textureResourceIds.clear();
        textureCoordinatesResourceIds.clear();
    }

    /**
     * Returns a new generated valid Texture id.
     *
     * @param gl
     * @return generatedId
     */
    private static int newTextureID(final GL10 gl) {
        final int[] temp = new int[1];
        gl.glGenTextures(1, temp, 0);
        return temp[0];
    }

    private void deleteTexture(int textureId) {
		int[] a = new int[1];
		a[0] = textureId;
		gl.glDeleteTextures(1, a, 0);
	}

    public String getNewAtlasId() {
        return "atlas" + (atlasId++);
//        return "atlas".concat(Integer.toString(atlasId++));
    }

    public boolean contains(String textureId) {
        return textureResourceIds.containsKey(textureId);
    }

    public int addTexture(Bitmap texture, String atlasId, boolean generateMipMap) {
        int textureId = alloc(texture,generateMipMap);
        textureResourceIds.put(atlasId,textureId);
        return textureId;
    }
}
