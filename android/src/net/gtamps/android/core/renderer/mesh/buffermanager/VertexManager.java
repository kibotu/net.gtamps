package net.gtamps.android.core.renderer.mesh.buffermanager;

import net.gtamps.android.core.renderer.mesh.Uv;
import net.gtamps.android.core.renderer.mesh.Vertex;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

public class VertexManager {

    private Vector3BufferManager vertices;
    private UvBufferManager uvs;
    private Vector3BufferManager normals;
    private Color4BufferManager colors;

    private boolean hasUvs;
    private boolean hasNormals;
    private boolean hasColors;

    public VertexManager(int maxElements) {
        this(maxElements, true, true, true);
    }

    public VertexManager(int maxElements, Boolean useUvs, Boolean useNormals, Boolean useColors) {
        vertices = new Vector3BufferManager(maxElements);

        hasUvs = useUvs;
        hasNormals = useNormals;
        hasColors = useColors;

        if (hasUvs) uvs = new UvBufferManager(maxElements);
        if (hasNormals) normals = new Vector3BufferManager(maxElements);
        if (hasColors) colors = new Color4BufferManager(maxElements);
    }

    public VertexManager(Vector3BufferManager points, UvBufferManager uvs, Vector3BufferManager normals, Color4BufferManager colors) {
        vertices = points;
        this.uvs = uvs;
        this.normals = normals;
        this.colors = colors;

        hasUvs = uvs != null && uvs.size() > 0;
        hasNormals = normals != null && normals.size() > 0;
        hasColors = colors != null && colors.size() > 0;
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
            float textureU, float textureV) {

        vertices.add(pointX, pointY, pointZ);

        if (hasUvs) uvs.add(textureU, textureV);
        if (hasNormals) normals.add(normalX, normalY, normalZ);
        if (hasColors) colors.add(colorR, colorG, colorB, colorA);

        return (short) (vertices.size() - 1);
    }

    public short addVertex(Vector3 point, Vector3 normal, Color4 color, Uv textureUv) {
        vertices.add(point);

        if (hasUvs) uvs.add(textureUv);
        if (hasNormals) normals.add(normal);
        if (hasColors) colors.add(color);

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
        return new VertexManager(vertices.clone(), uvs.clone(), normals.clone(), colors.clone());
    }

    public void addAll(Vertex... vertices) {
        for (Vertex vertex : vertices) {
            addVertex(vertex.position, vertex.normal, vertex.color, vertex.uv);
        }
    }
}