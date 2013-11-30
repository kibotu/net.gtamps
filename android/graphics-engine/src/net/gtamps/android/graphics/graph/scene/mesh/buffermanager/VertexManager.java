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
    private WeightManager weights;

    private boolean hasNormals;
    private boolean hasUvs;
    private boolean hasBones;

    public VertexManager(int maxElements, boolean useNormals, boolean useUvs, boolean useBones) {
        vertices = new Vector3BufferManager(maxElements);

        hasNormals = useNormals;
        hasUvs = useUvs;
        hasBones = useBones;

        if (hasUvs) uvs = new UvBufferManager(maxElements);
        if (hasNormals) normals = new Vector3BufferManager(maxElements);
        if (hasBones) weights = new WeightManager(maxElements);
    }

    public VertexManager(Vector3BufferManager points, UvBufferManager uvs, Vector3BufferManager normals, WeightManager weights) {
        vertices = points;
        this.uvs = uvs;
        this.normals = normals;
        this.weights = weights;

        hasUvs = uvs != null && uvs.size() > 0;
        hasNormals = normals != null && normals.size() > 0;
        hasBones = weights != null && weights.size() > 0;
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

    public short addVertex(
            float pointX, float pointY, float pointZ,
            float normalX, float normalY, float normalZ,
            float textureU, float textureV,
            float weightX, float weighY, float weightZ, float weightW,
            int influence1, int influence2, int influence3, int influence4) {

        vertices.add(pointX, pointY, pointZ);

        if (hasUvs) uvs.add(textureU, textureV);
        if (hasNormals) normals.add(normalX, normalY, normalZ);
        if (hasBones) weights.add(weightX,weighY,weightZ,weightW,influence1,influence2,influence3,influence4);

        return (short) (vertices.size() - 1);
    }

    public short addVertex(
            float pointX, float pointY, float pointZ,
            float normalX, float normalY, float normalZ,
            float textureU, float textureV) {
        return addVertex(pointX,pointY,pointZ,normalX,normalY,normalZ,textureU,textureV,0,0,0,0,0,0,0,0);
    }

    public short addVertex(Vector3 point, Vector3 normal, Uv uv) {
        return addVertex(point,normal,uv, new Weight());
    }

    public short addVertex(Vector3 point, Vector3 normal, Uv uv, Weight weight) {
        vertices.add(point);

        if (hasNormals) normals.add(normal);
        if (hasUvs) uvs.add(uv);
        if (hasBones) weights.add(weight);

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

    public VertexManager clone() {
        return new VertexManager(
                vertices == null ? null: vertices.clone(),
                uvs == null ? null: uvs.clone(),
                normals == null ? null: normals.clone(),
                weights == null ? null: weights.clone());
    }

    public void addAll(Vertex... vertices) {
        for (Vertex vertex : vertices) {
            addVertex(vertex.position, vertex.normal, vertex.uv, vertex.weights);
        }
    }

    public WeightManager getWeights() {
        return weights;
    }

    public boolean hasBones() {
        return hasBones;
    }
}