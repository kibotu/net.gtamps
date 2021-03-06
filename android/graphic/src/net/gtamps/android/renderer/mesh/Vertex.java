package net.gtamps.android.renderer.mesh;


import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

/**
 * Container holding VO's of vertex-related information.
 * Not required for operation of framework, but may be helpful as a convenience.
 */
public class Vertex {

    public Vector3 position = Vector3.createNew();
    public Uv uv;
    public Vector3 normal;
    public Color4 color;

    public Vertex() {
    }
}
