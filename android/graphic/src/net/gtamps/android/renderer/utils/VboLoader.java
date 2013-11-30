package net.gtamps.android.renderer.utils;

import android.content.Context;
import net.gtamps.android.renderer.mesh.buffermanager.Vbo;
import net.gtamps.android.utils.XmlLoader;
import org.jdom.Document;
import org.jdom.Element;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

public class VboLoader {

    private static final String TAG = VboLoader.class.getSimpleName();

    private static HashMap<Integer, Vbo> vboCache;

    /**
     * Amount of Byes a Float has.
     */
    public static final int FLOAT_SIZE = 4;

    /**
     * Amount of Bytes a Short has.
     */
    public static final int SHORT_SIZE = 2;

    // static
    private VboLoader() {
    }

    public static Vbo loadVbo(Context context, int resourceId) {

        if (vboCache == null) {
            vboCache = new HashMap<Integer, Vbo>();
        }
        if (vboCache.containsKey(resourceId)) {
            return vboCache.get(resourceId);
        }

        final Document doc = XmlLoader.loadXml(context, resourceId);
        final Element root = doc.getRootElement();

        final Element verticesElement = root.getChild("vertices");
        final Element indicesElement = root.getChild("indices");
        final Element normalsElement = root.getChild("normals");
        final Element colorsElement = root.getChild("colors").getChild("diffuse");
        final Element texturesElement = root.getChild("textureCoords");

        final float[] vertices = parseFloats(verticesElement);
        final short[] indices = parseShorts(indicesElement);
        final float[] normals = parseFloats(normalsElement);
        final float[] colors = parseFloats(colorsElement);
        final float[] textureCoords = parseFloats(texturesElement);

        // allocate buffers
        FloatBuffer vertexBuffer = OpenGLUtils.toFloatBufferPositionZero(vertices);
        ShortBuffer indexBuffer = OpenGLUtils.toShortBuffer(indices);
        FloatBuffer normalBuffer = OpenGLUtils.toFloatBufferPositionZero(normals);
        FloatBuffer colorBuffer = (colors.length > 1) ? OpenGLUtils.toFloatBufferPositionZero(colors) : null;
        FloatBuffer textureBuffer = (textureCoords.length > 1) ? OpenGLUtils.toFloatBufferPositionZero(textureCoords) : null;

        Vbo vbo = new Vbo().set(vertexBuffer, indexBuffer, normalBuffer, colorBuffer, textureBuffer);
        vboCache.put(resourceId, vbo);
        return vbo;
    }

    private static short[] parseShorts(Element element) {
        String[] values = element.getText().split(" ");
        short[] result = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Short.parseShort(values[i]);
        }
        return result;
    }

    private static float[] parseFloats(Element element) {
        String[] values = element.getText().split(" ");
        float[] result = new float[values.length];
        if (values.length < 2) {
            return result;
        }
        for (int i = 0; i < values.length; i++) {
            result[i] = Float.parseFloat(values[i]);
        }
        return result;
    }
}

