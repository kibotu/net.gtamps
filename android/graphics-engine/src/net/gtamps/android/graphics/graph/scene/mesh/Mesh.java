package net.gtamps.android.graphics.graph.scene.mesh;

import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.FaceManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vbo;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.VertexManager;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureManager;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:02
 */
public class Mesh {

    public final VertexManager vertices;
    public final TextureManager textures;
    public final FaceManager faces;
    public Vbo vbo;

    public Mesh(int maxFaces, int maxVertices) {
        faces = new FaceManager(maxFaces);
        vertices = new VertexManager(maxVertices);
        textures = new TextureManager();
    }

    public Mesh(Mesh mesh) {
        faces = mesh.faces.clone();
        vertices = mesh.vertices.clone();
        textures = mesh.textures.clone();
    }

    public void allocate() {
        positionZero();
        if (!Config.USEVBO) return;
        if (vbo != null && vbo.isAllocated) return;
        vbo = Registry.getRenderer().allocBuffers(
                vertices.getVertices().getBuffer(),
                vertices.getNormals().getBuffer(),
                vertices.getColors().getBuffer(),
                vertices.getUvs().getBuffer(),
                faces.getBuffer());
    }

    /**
     * Sets buffer position to 0.
     */
    private void positionZero() {
        if (vertices.getVertices().getBuffer() != null) vertices.getVertices().getBuffer().position(0);
        if (vertices.getNormals().getBuffer() != null) vertices.getNormals().getBuffer().position(0);
        if (vertices.getColors().getBuffer() != null) vertices.getColors().getBuffer().position(0);
        if (vertices.getUvs().getBuffer() != null) vertices.getUvs().getBuffer().position(0);
        if (faces.getBuffer() != null) faces.getBuffer().position(0);
    }

    /**
     * Adds a new Vertex.
     *
     * @param vx x-position
     * @param vy y-position
     * @param vz z-position
     * @param nx x-normal
     * @param ny y-normal
     * @param nz z-normal
     * @param cr color red
     * @param cg color green
     * @param cb color blue
     * @param ca color alpha
     * @param u  texture coordinate x
     * @param v  texture coordinate y
     */
    public void addVertex(float vx, float vy, float vz, float nx, float ny, float nz, float cr, float cg, float cb, float ca, float u, float v) {
        vertices.addVertex(Vector3.createNew(vx, vy, vz), Vector3.createNew(nx, ny, nz), new Color4(cr, cg, cb, ca), new Uv(u, v));
    }

    /**
     * Adds a vertex.
     *
     * @param point
     * @param normal
     * @param color
     * @param uv
     */
    public void addVertex(Vector3 point, Vector3 normal, Color4 color, Uv uv) {
        vertices.addVertex(point, normal, color, uv);
    }

    /**
     * Adds an array of vertices.
     *
     * @param vertices
     */
    public void addVertex(Vertex... vertices) {
        this.vertices.addAll(vertices);
    }

    public void invalidate() {
        if(vbo != null) vbo.isAllocated = false;
    }

    public void update() {
        if(vbo != null) Registry.getRenderer().update(this);
    }

    public Mesh clone() {
        return new Mesh(this);
    }
}
