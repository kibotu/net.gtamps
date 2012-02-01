package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
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

        final float c = 1f;
        Color4 emissive = material.getEmission();

        mesh.addVertex(c, -c, -c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, -c, c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, -c, c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);

        mesh.faces.add(0, 1, 2);
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }

    @Override
    public void onResumeInternal(GL10 gl10) {
    }

    @Override
    protected void onTransformationInternal(GL10 gl10) {
    }
}

