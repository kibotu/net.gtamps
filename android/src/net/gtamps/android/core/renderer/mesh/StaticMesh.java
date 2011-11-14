package net.gtamps.android.core.renderer.mesh;

public class StaticMesh extends Mesh {

    public StaticMesh(Mesh mesh) {
        super(0,0);
        this.vbo = mesh.vbo;
    }
}
