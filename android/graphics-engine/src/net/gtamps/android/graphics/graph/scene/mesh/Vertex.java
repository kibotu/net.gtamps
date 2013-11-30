package net.gtamps.android.graphics.graph.scene.mesh;


import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

/**
 * Container holding VO's of vertex-related information.
 * Not required for operation of framework, but may be helpful as a convenience.
 */
public class Vertex {

    public Vector3 position;
    public Uv uv;
    public Vector3 normal;
    public Weight weights;

    public Vertex(Vector3 position, Uv uv, Vector3 normal, Weight weights) {
        this.position = position;
        this.uv = uv;
        this.normal = normal;
        this.weights = weights;
    }

    public Vertex() {
        this.position = Vector3.createNew();
        this.uv = new Uv();
        this.normal = Vector3.createNew();
        this.weights = new Weight();
    }
}
