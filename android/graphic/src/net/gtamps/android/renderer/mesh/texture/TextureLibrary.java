package net.gtamps.android.renderer.mesh.texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import net.gtamps.android.renderer.Registry;
import net.gtamps.android.renderer.utils.TextureCoordinateLoader;

import javax.microedition.khronos.opengles.GL10;
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

        if (textureCoordinatesResourceIds.containsKey(textureCoordinateResourceId)) {
            return textureCoordinatesResourceIds.get(textureCoordinateResourceId);
        }
        BufferedTexture bufferedTexture = TextureCoordinateLoader.loadTextureCoordinates(gl, Registry.getContext(), textureCoordinateResourceId, loadTexture(textureResourceId, generateMipMap));
        textureCoordinatesResourceIds.put(textureCoordinateResourceId, bufferedTexture);

        return bufferedTexture;
    }

    public int loadTexture(final int textureResourceId, boolean generateMipMap) {
        return loadTexture(textureResourceId, generateMipMap, Bitmap.Config.ARGB_8888, false);
    }

    public int loadTexture(final int textureResourceId, boolean generateMipMap, Bitmap.Config bitmapConfig, boolean flipped) {
        if (textureResourceIds.containsKey("" + textureResourceId)) {
            return textureResourceIds.get("" + textureResourceId);
        }

        BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
        sBitmapOptions.inPreferredConfig = bitmapConfig;
        sBitmapOptions.inScaled = false;

        Bitmap bitmap;
        if (flipped) {
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

        int id = Registry.getRenderer().allocTexture(bitmap, generateMipMap);
        textureResourceIds.put("" + textureResourceId, id);

        return id;
    }

    /**
     * Clear if no textures need to be loaded again.
     */
    public void clear() {
        textureResourceIds.clear();
        textureCoordinatesResourceIds.clear();
    }

    public String getNewAtlasId() {
        return "atlas" + (atlasId++);
    }

    public boolean contains(String textureId) {
        return textureResourceIds.containsKey(textureId);
    }

    public int addTexture(Bitmap texture, String atlasId, boolean generateMipMap) {
        int textureId = Registry.getRenderer().allocTexture(texture, generateMipMap);
        textureResourceIds.put(atlasId, textureId);
        return textureId;
    }

    public void invalidate() {
        // TODO re-allocate all loaded textures
    }
}
