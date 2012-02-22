package net.gtamps.android.graphics.graph.scene.mesh.texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import net.gtamps.android.graphics.utils.OpenGLUtils;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.android.utils.XmlLoader;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.state.State;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Class loads a texture on-demand.
 */
public class TextureLibrary {

    private static final String TAG = TextureLibrary.class.getSimpleName();

    public HashMap<String, Integer> getTextureResourceIDs() {
        return textureResourceIDs;
    }

    public HashMap<Integer, TextureAnimation> getUvResourceID() {
        return uvResourceID;
    }

    /**
     * Holds loaded TextureIds. In order to keep track of loaded textures.
     */
    private HashMap<String, Integer> textureResourceIDs;

    /**
     * Holds Buffered Texture Coordinates in order to keep track of loaded xml resource ids.
     */
    private HashMap<Integer, TextureAnimation> uvResourceID;
    private static int atlasId = 0;

    /**
     * Constructs the texture proxy.
     *
     * @param gl
     */
    public TextureLibrary(GL10 gl) {
        textureResourceIDs = new HashMap<String, Integer>(10);
        uvResourceID = new HashMap<Integer, TextureAnimation>(10);
    }

    /**
     * Loads a uv sheet.
     *
     * @param uvResourceID
     * @return uv coordinate sheet
     */
    public TextureAnimation loadBufferedTexture(int uvResourceID) {

        if (this.uvResourceID.containsKey(uvResourceID)) {
            return this.uvResourceID.get(uvResourceID);
        }
        TextureAnimation textureAnimation = loadTextureCoordinates(uvResourceID);
        this.uvResourceID.put(uvResourceID, textureAnimation);

        return textureAnimation;
    }

    /**
     * More convenient method to load a texture.
     *
     * @param textureResourceId
     * @param generateMipMap
     * @return textureID
     */
    public int loadTexture(final int textureResourceId, boolean generateMipMap) {
        return loadTexture(textureResourceId, generateMipMap, Bitmap.Config.ARGB_8888, false);
    }

    /**
     * Loads a texture by texture resource id.
     *
     * @param textureResourceId
     * @param generateMipMap
     * @param bitmapConfig
     * @param flipped
     * @return hardware generated textureID
     */
    public int loadTexture(final int textureResourceId, boolean generateMipMap, Bitmap.Config bitmapConfig, boolean flipped) {
        if (textureResourceIDs.containsKey("" + textureResourceId)) {
            return textureResourceIDs.get("" + textureResourceId);
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
        textureResourceIDs.put("" + textureResourceId, id);

        return id;
    }

    /**
     * Clear if no textures need to be loaded again.
     */
    public void clear() {
        textureResourceIDs.clear();
        uvResourceID.clear();
    }

    public String getNewAtlasId() {
        return "atlas" + (atlasId++);
    }

    public boolean contains(String textureId) {
        return textureResourceIDs.containsKey(textureId);
    }

    public int addTexture(Bitmap texture, String atlasId, boolean generateMipMap) {
        int textureId = Registry.getRenderer().allocTexture(texture, generateMipMap);
        textureResourceIDs.put(atlasId, textureId);
        return textureId;
    }

    /**
     * Loads and parses a texture coordinate xml file and binds coordinate buffers.
     *
     * @param gl
     * @param context
     * @param textureCoordinatesResourceId
     */
    public static TextureAnimation loadTextureCoordinates(int textureCoordinatesResourceId) {

        // load xml file
        Document doc = XmlLoader.loadXml(Registry.getContext().getApplicationContext(), textureCoordinatesResourceId);

        // put all images into a list
        // jdom lets world explode if xml file corrupted.
        @SuppressWarnings("unchecked")
        List<Element> list = doc.getRootElement().getChildren("img");

        // to keep track of width, height and if it is loaded.
        TextureSprite[] gltextures = new TextureSprite[list.size()];

        // start building float buffer
        ArrayList<Float> coordinates = new ArrayList<Float>();

        // magic number offset multipler
        final int stride = 32;

        // all sprite image texture coordinates need to be together as one float array
        for (int i = 0; i < list.size(); ++i) {
            try {
                float x = list.get(i).getAttribute("x").getFloatValue();
                float y = list.get(i).getAttribute("y").getFloatValue();
                float w = list.get(i).getAttribute("w").getFloatValue();
                float h = list.get(i).getAttribute("h").getFloatValue();
                addTextureCoord(coordinates, x, y, w, h);
                gltextures[i] = new TextureSprite(w, h, i * stride);
            } catch (DataConversionException e) {
                Logger.i(TAG, "Texture Coordinate loading failed. Texture Coordinate Resource Id: " + textureCoordinatesResourceId);
                Logger.i(TAG, e.getMessage());
            }
        }

        // get buffer id
        int bufferId = Registry.getRenderer().allocate(OpenGLUtils.toFloatBufferPositionZero(coordinates));

        // get animations
        HashMap<State.Type, Integer[]> animations = parseAnimation(doc.getRootElement().getChildren("animation"));

        Logger.i(TAG, "[sprites:" + gltextures.length + "|animations:" + animations.size() + "|id:" + bufferId + "] Texture Coordinates successfully loaded.");

        // return new xml texture
        return new TextureAnimation(bufferId, gltextures, animations);
    }

    /**
     * Parses animations indices mapped by the state type.
     *
     * @param list
     * @return animations indices
     */
    private static HashMap<State.Type, Integer[]> parseAnimation(List<Element> list) {

        HashMap<State.Type, Integer[]> animations = new HashMap<State.Type, Integer[]>();

        for (Element e : list) {
            try {
                animations.put(State.Type.valueOf(e.getAttributeValue("type").toUpperCase()), parseIndices(e.getAttributeValue("indices")));
            } catch (NumberFormatException ex) {
                Logger.i(TAG, "Animation indices aren't correctly formated in xml file (indices must be integers)");
                Logger.i(TAG, ex.getMessage());
            }
        }

        return animations;
    }

    /**
     * Converts a map of indices to animations and stores them into a new hashmap.
     *
     * @param textureSprites
     * @param indexedAnims
     * @return HashMap with animations.
     */
    public static HashMap<State.Type, TextureSprite[]> convertIndicesToAnimations(@NotNull TextureSprite[] textureSprites, @NotNull HashMap<State.Type, Integer[]> indexedAnims) {

        HashMap<State.Type, TextureSprite[]> anims = new HashMap<State.Type, TextureSprite[]>();

        for (HashMap.Entry<State.Type, Integer[]> entry : indexedAnims.entrySet()) {
            Integer[] temp = entry.getValue();
            TextureSprite[] textures = new TextureSprite[temp.length];
            for (int i = 0; i < temp.length; i++) {
                textures[i] = textureSprites[temp[i]];
            }
            anims.put(entry.getKey(), textures);
        }

        indexedAnims.clear();

        return anims;
    }

    /**
     * Parses an Integer array out of a String full of integers.
     *
     * @param values
     * @return int array
     */
    private static Integer[] parseIndices(String values) throws NumberFormatException {
        String[] index = values.split(" ");
        Integer[] indices = new Integer[index.length];
        for (int i = 0; i < index.length; ++i) {
            indices[i] = Integer.parseInt(index[i]);
        }
        return indices;
    }

    /**
     * Adds a single set of texture coordinates to the list.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void addTextureCoord(List<Float> list, final float x, final float y, final float width, final float height) {

        // for texture coordinate float buffer purpose
        list.add(x + width);
        list.add(y);            // oben rechts
        list.add(x);
        list.add(y);            // oben links
        list.add(x);
        list.add(y + height);   // unten links
        list.add(x + width);
        list.add(y + height);   // unten rechts
    }
}
