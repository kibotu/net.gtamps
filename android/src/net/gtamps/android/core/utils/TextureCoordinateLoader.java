package net.gtamps.android.core.utils;

import android.content.Context;
import net.gtamps.android.core.renderer.mesh.texture.BufferedTexture;
import net.gtamps.android.core.renderer.mesh.texture.SpriteTetxure;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.state.State;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextureCoordinateLoader {

    private static final String TAG = TextureCoordinateLoader.class.getSimpleName();

    // static
    private TextureCoordinateLoader() {
    }

    /**
     * Loads and parses a texture coordinate xml file and binds coordinate buffers.
     *
     * @param gl
     * @param context
     * @param textureCoordinatesResourceId
     */
    public static BufferedTexture loadTextureCoordinates(GL10 gl, Context context, int textureCoordinatesResourceId, int textureId) {

        // load xml file
        Document doc = XmlLoader.loadXml(context, textureCoordinatesResourceId);

        // put all images into a list
        // jdom lets world explode if xml file corrupted.
        @SuppressWarnings("unchecked")
        List<Element> list = doc.getRootElement().getChildren("img");

        // to keep track of width, height and if it is loaded.
        SpriteTetxure[] gltextures = new SpriteTetxure[list.size()];

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
                gltextures[i] = new SpriteTetxure(w, h, i * stride);
            } catch (DataConversionException e) {
                Logger.i(TAG, "Texture Coordinate loading failed. Texture Coordinate Resource Id: " + textureCoordinatesResourceId);
                Logger.i(TAG, e.getMessage());
            }
        }

        // get buffer id
        int bufferId = allocate(gl, OpenGLUtils.toFloatBufferPositionZero(coordinates));

        // get animations
        HashMap<State.Type, Integer[]> animations = parseAnimation(doc.getRootElement().getChildren("animation"));

        Logger.i(TAG, "[sprites:" + gltextures.length + "|animations:" + animations.size() + "|id:" + bufferId + "] Texture Coordinates successfully loaded.");

        // return new xml texture
        return new BufferedTexture(textureId, bufferId, gltextures, animations);
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
     * Allocates a float buffer and returns the generated id to which it has been bound.
     * For convenience it needs OpenGL 1.1 to generate a buffer id.
     *
     * @param gl
     * @param floatBuffer
     * @return generated id
     */
    private static int allocate(GL10 gl, FloatBuffer floatBuffer) {

        GL11 gl11 = (GL11) gl;
        if (gl11 == null) {
            Logger.e(TAG, "No OpenGL 1.1 Support");
            throw new NullPointerException("No OpenGL 1.1 Support");
        }

        // generate id
        final IntBuffer buffer = IntBuffer.allocate(1);
        gl11.glGenBuffers(1, buffer);
        int id = buffer.get(0);

        // bind float buffer to generated id
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id);
        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, floatBuffer.capacity() * 4, floatBuffer, GL11.GL_STATIC_DRAW);

        // return id so it can be remembered
        return id;
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
