package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public class Sphere extends RenderableNode {

    private static Mesh mesh;

    /**
     * Sphere stacks from pole to pole.
     */
    private int stacks;
    /**
     * Sphere slices around the equator.
     */
    private int slices;

    public Sphere(float radius, int stacks, int slices) {
        dimension.set(radius, radius, radius);
        this.stacks = stacks;
        this.slices = slices;
    }

    public Sphere() {
        this(1, 20, 20);
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;

        mesh = new Mesh(stacks * slices * 2,(stacks + 1) * (slices + 1));

        Color4 emissive = material.getEmission();

        int r, c;

        Vector3 n = Vector3.createNew();
        Vector3 pos = Vector3.createNew();
        Vector3 posFull = Vector3.createNew();

        // vertices
        for (r = 0; r <= slices; r++) {
            float v = (float) r / (float) slices; // [0,1]
            float theta1 = v * (float) Math.PI; // [0,PI]

            n.set(0, 1, 0);
            n.rotateZ(theta1);

            for (c = 0; c <= stacks; c++) {
                float u = (float) c / (float) stacks; // [0,1]
                float theta2 = u * (float) (Math.PI * 2f); // [0,2PI]
                pos.set(n);
                pos.rotateY(theta2);

                posFull.set(pos);
                posFull.mulInPlace(dimension.x);

                mesh.vertices.addVertex(posFull.x, posFull.y, posFull.z, pos.x, pos.y, pos.z, emissive.r, emissive.g, emissive.b, emissive.a, u, v);
            }
        }

        // faces
        int colLength = stacks + 1;

        for (r = 0; r < slices; r++) {
            int offset = r * colLength;

            for (c = 0; c < stacks; c++) {
                int ul = offset + c;
                int ur = offset + c + 1;
                int br = offset + (int) (c + 1 + colLength);
                int bl = offset + (int) (c + 0 + colLength);

                mesh.faces.addQuad(ul, ur, br, bl);
            }
        }

        n.recycle();
        pos.recycle();
        posFull.recycle();

        mesh.allocate();
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }

    @Override
    protected void onTransformationInternal(GL10 gl10) {
    }
}
