package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.shared.Utils.math.Color4;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public class Plane extends RenderableNode {

    private static Mesh mesh;

    public Plane() {
        this(1, 1, 1);
    }

    public Plane(float width, float height, float depth) {
        dimension.set(width, height, depth);
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;

        mesh = new Mesh(2, 4);
        Color4 emissive = material.getEmission();

        /**
         * 1----0
         * |  / |
         * | /  |
         * 2----3
         */

        // oben rechts
        mesh.addVertex(0.5f, 0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 1, 0);
        // oben links
        mesh.addVertex(-0.5f, 0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        // unten links
        mesh.addVertex(-0.5f, -0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 0, 1);
        // unten rechts
        mesh.addVertex(0.5f, -0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 1, 1);

        mesh.faces.add(0, 1, 2);
        mesh.faces.add(2, 3, 0);

        mesh.allocate();
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }
}
