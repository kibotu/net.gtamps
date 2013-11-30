package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.shared.Utils.math.Color4;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:25
 */
public class Triangle extends RenderableNode {

    private static Mesh mesh;

    public Triangle() {
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;

        mesh = new Mesh(1, 3);

        addUperLeftTriangle(mesh);
//        addLowerRightTriangle(mesh);

        mesh.allocate();
    }

    private void addUperLeftTriangle(Mesh mesh) {

        final float c = 0.5f;
        Color4 emissive = material.getEmission();

        /**
         * 1----0
         * |  /
         * | /
         * 2
         */

        // oben rechts
        mesh.addVertex(0.5f, 0.5f, 0, 0, 0, 1, 1, 0);
        // oben links
        mesh.addVertex(-0.5f, 0.5f, 0, 0, 0, 1, 0, 0);
        // unten links
        mesh.addVertex(-0.5f, -0.5f, 0, 0, 0, 1, 0, 1);

        mesh.faces.add(0, 1, 2);
    }

    private void addLowerRightTriangle(Mesh mesh) {

        final float c = 0.5f;

        /**
         *      0
         *    / |
         *   /  |
         * 1----2
         */

        // oben rechts
        mesh.addVertex(0.5f, 0.5f, 0, 0, 0, 1, 1, 0);
        // unten links
        mesh.addVertex(-0.5f, -0.5f, 0, 0, 0, 1, 0, 1);
        // unten rechts
        mesh.addVertex(0.5f, -0.5f, 0, 0, 0, 1, 1, 1);

        mesh.faces.add(0, 1, 2);
    }


    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }
}

