package net.gtamps.android.graphics.graph.scene.mesh.buffermanager;

import android.graphics.YuvImage;
import net.gtamps.android.graphics.graph.scene.mesh.Uv;
import net.gtamps.android.graphics.graph.scene.mesh.Vertex;
import net.gtamps.android.graphics.graph.scene.mesh.Weight;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

public class VertexManager {

    private Vector3BufferManager vertices;
    private UvBufferManager uvs;
    private Vector3BufferManager normals;
    private Color4BufferManager colors;
    private WeightManager weights;

    private boolean hasUvs;
    private boolean hasNormals;
    private boolean hasColors;
    private boolean hasWeights;

    public VertexManager(int maxElements) {
        this(maxElements, true, true, true,false);
    }

    public VertexManager(int maxElements, Boolean useUvs, Boolean useNormals, Boolean useColors, boolean useWeights) {
        vertices = new Vector3BufferManager(maxElements);

        hasUvs = useUvs;
        hasNormals = useNormals;
        hasColors = useColors;
        hasWeights = useWeights;

        if (hasUvs) uvs = new UvBufferManager(maxElements);
        if (hasNormals) normals = new Vector3BufferManager(maxElements);
        if (hasColors) colors = new Color4BufferManager(maxElements);
        if (hasWeights) weights = new WeightManager(maxElements);
    }

    public VertexManager(Vector3BufferManager points, UvBufferManager uvs, Vector3BufferManager normals, Color4BufferManager colors, WeightManager weights) {
        vertices = points;
        this.uvs = uvs;
        this.normals = normals;
        this.colors = colors;
        this.weights = weights;

        hasUvs = uvs != null && uvs.size() > 0;
        hasNormals = normals != null && normals.size() > 0;
        hasColors = colors != null && colors.size() > 0;
        hasWeights = weights != null && weights.size() > 0;
    }

    public int size() {
        return vertices.size();
    }

    public int capacity() {
        return vertices.capacity();
    }

    public boolean hasUvs() {
        return hasUvs;
    }

    public boolean hasNormals() {
        return hasNormals;
    }

    public boolean hasColors() {
        return hasColors;
    }

    public short addVertex(
            float pointX, float pointY, float pointZ,
            float normalX, float normalY, float normalZ,
            float colorR, float colorG, float colorB, float colorA,
            float textureU, float textureV,
            float weightX, float weighY, float weightZ, float weightW,
            int influence1, int influence2, int influence3, int influence4) {

        vertices.add(pointX, pointY, pointZ);

        if (hasUvs) uvs.add(textureU, textureV);
        if (hasNormals) normals.add(normalX, normalY, normalZ);
        if (hasColors) colors.add(colorR, colorG, colorB, colorA);
        if (hasWeights) weights.add(weightX,weighY,weightZ,weightW,influence1,influence2,influence3,influence4);

        return (short) (vertices.size() - 1);
    }

    public short addVertex(
            float pointX, float pointY, float pointZ,
            float normalX, float normalY, float normalZ,
            float colorR, float colorG, float colorB, float colorA,
            float textureU, float textureV) {
        return addVertex(pointX,pointY,pointZ,normalX,normalY,normalZ,colorR,colorG,colorB,colorA,textureU,textureV,0,0,0,0,0,0,0,0);
    }

    public short addVertex(Vector3 point, Vector3 normal, Color4 color, Uv uv) {
        return addVertex(point,normal,color,uv, new Weight());
    }

    public short addVertex(Vector3 point, Vector3 normal, Color4 color, Uv uv, Weight weight) {
        vertices.add(point);

        if (hasUvs) uvs.add(uv);
        if (hasNormals) normals.add(normal);
        if (hasColors) colors.add(color);
        if (hasWeights) weights.add(weight);

        return (short) (vertices.size() - 1);
    }

    public void overwriteVerts(float[] newVerts) {
        vertices.overwrite(newVerts);
    }

    public void overwriteNormals(float[] newNormals) {
        normals.overwrite(newNormals);
    }

    public Vector3BufferManager getVertices() {
        return vertices;
    }

    /**
     * List of texture coordinates
     */
    public UvBufferManager getUvs() {
        return uvs;
    }

    /**
     * List of normal values
     */
    public Vector3BufferManager getNormals() {
        return normals;
    }

    /**
     * List of color values
     */
    public Color4BufferManager getColors() {
        return colors;
    }

    public VertexManager clone() {
        return new VertexManager(
                vertices == null ? null: vertices.clone(),
                uvs == null ? null: uvs.clone(),
                normals == null ? null: normals.clone(),
                colors == null ? null: colors.clone(),
                weights == null ? null: weights.clone());
    }

    public void addAll(Vertex... vertices) {
        for (Vertex vertex : vertices) {
            addVertex(vertex.position, vertex.normal, vertex.color, vertex.uv, vertex.weights);
        }
    }

    public WeightManager getWeights() {
        return weights;
    }
}