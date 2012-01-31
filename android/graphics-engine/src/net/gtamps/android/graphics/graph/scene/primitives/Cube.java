package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public class Cube extends RenderableNode {

    private static Mesh mesh;

    public Cube() {
        this(24, 12);
    }

    public Cube(int faces, int maxVertices) {
        if(mesh == null) mesh = new Mesh(faces,maxVertices);
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }
}
