package net.gtamps.android.renderer.mesh;

import net.gtamps.android.renderer.mesh.buffermanager.FaceManager;
import net.gtamps.android.renderer.mesh.buffermanager.Vbo;
import net.gtamps.android.renderer.mesh.buffermanager.VertexManager;
import net.gtamps.android.renderer.mesh.texture.TextureManager;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Mesh {

    // serializer side
    public final VertexManager vertices;
    public final TextureManager textures;
    public final FaceManager faces;

    // gpu-server-side
    protected Vbo vbo = new Vbo();

    public Mesh(int maxAmountVertex, int maxAmountFaces) {
        vertices = new VertexManager(maxAmountVertex);
        textures = new TextureManager();
        faces = new FaceManager(maxAmountFaces);
    }

    public Mesh(@NotNull Mesh mesh) {
        this.vbo.set(mesh.vbo);
        vertices = null;
        textures = null;
        faces = null;
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

    public void addVertex(Vector3 point, Vector3 normal, Color4 color, Uv uv) {
        vertices.addVertex(point, normal, color, uv);
    }

    public void setup(GL10 gl) {
        if (vbo.isAllocated()) return;
        vbo.set(vertices.getVertices().getBuffer(), faces.getBuffer(), vertices.getNormals().getBuffer(), vertices.getColors().getBuffer(), vertices.getUvs().getFloatBuffer());
        vbo.allocBuffers(gl);
    }

    public void addVertex(Vertex... vertices) {
        this.vertices.addAll(vertices);
    }

    public Vbo getVbo() {
        return vbo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mesh)) return false;

        Mesh mesh = (Mesh) o;

        if (!faces.equals(mesh.faces)) return false;
        if (!textures.equals(mesh.textures)) return false;
        if (!vbo.equals(mesh.vbo)) return false;
        if (!vertices.equals(mesh.vertices)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + textures.hashCode();
        result = 31 * result + faces.hashCode();
        result = 31 * result + vbo.hashCode();
        return result;
    }

    public void invalidate() {
        vbo.invalidate();
    }
}
