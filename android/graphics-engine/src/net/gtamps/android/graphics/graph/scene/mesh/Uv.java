package net.gtamps.android.graphics.graph.scene.mesh;

/**
 * Simple VO used for texture positioning
 */
public class Uv {

    public float u;
    public float v;

    public Uv() {
        u = 0;
        v = 0;
    }

    public Uv(float u, float v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public Uv clone() {
        return new Uv(u, v);
    }

    @Override
    public String toString() {
        return "Uv{" +
                "u=" + u +
                ", v=" + v +
                '}';
    }
}
