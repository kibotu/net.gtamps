package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:25
 */
public class Triangle extends RenderableNode {

    private static Mesh mesh;

    public Triangle() {
        this(3, 1);
    }

    public Triangle(int faces, int maxVertices) {
        if(mesh == null) mesh = new Mesh(faces,maxVertices);
    }

    @Override
    public void onCreate(GL10 gl10) {
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }
}

