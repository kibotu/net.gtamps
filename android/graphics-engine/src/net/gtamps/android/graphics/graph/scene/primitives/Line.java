package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe
 * Date: 02/12/12
 * Time: 11:32
 */
public class Line extends RenderableNode {

    private static Mesh mesh;
    private final Vector3 a;
    private final Vector3 b;
    private final float thickness;

    public Line(Vector3 a, Vector3 b, float thickness) {
       this(a.x, a.y, a.z, b.x, b.y, b.z, thickness);
    }

    public Line(float aX, float aY, float aZ, float bX, float bY, float bZ, float thickness) {
        this.a = Vector3.createNew(aX,aY,aZ);
        this.b = Vector3.createNew(bX,bY,bZ);
        this.thickness = thickness;
        getRenderState().setDoubleSided(true);
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;

        mesh = new Mesh(2, 4);

        /**
         * 1----0
         * |  / |
         * | /  |
         * 2----3
         */

        // oben rechts
        mesh.addVertex(a.x - thickness, a.z + thickness, a.z + 0, 0, 0, 1, 1, 0);
        // oben links
        mesh.addVertex(a.x -thickness, a.y + thickness, a.z + 0, 0, 0, 1, 0, 0);
        // unten links
        mesh.addVertex(b.x -thickness, b.y -thickness, b.z + 0, 0, 0, 1, 0, 1);
        // unten rechts
        mesh.addVertex(b.x + thickness, b.y -thickness, b.z + 0, 0, 0, 1, 1, 1);

        mesh.faces.add(0, 1, 2);
        mesh.faces.add(2, 3, 0);

        mesh.allocate();
    }



    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }
}
