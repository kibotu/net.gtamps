package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSample;
import net.gtamps.shared.Utils.math.Color4;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public class Cube extends RenderableNode {

    private static Mesh mesh;

    public Cube() {
        this(1, 1, 1);
    }

    public Cube(float width, float height, float depth) {
        dimension.set(width, height, depth);
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;

        mesh = new Mesh(12, 24);

        final float c = 1f;
        Color4 emissive = material.getEmission();

        mesh.addVertex(c, -c, -c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, -c, c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, -c, c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, -c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, c, -c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(-c, c, -c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(c, c, c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, -c, -c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, c, -c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(c, c, c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(c, -c, c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, -c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(-c, -c, c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(-c, c, c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, -c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, -c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, -c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, -c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);

        mesh.faces.add(0, 1, 2);
        mesh.faces.add(0, 2, 3);
        mesh.faces.add(4, 5, 6);
        mesh.faces.add(4, 6, 7);
        mesh.faces.add(8, 9, 10);
        mesh.faces.add(8, 10, 11);
        mesh.faces.add(12, 13, 14);
        mesh.faces.add(12, 14, 15);
        mesh.faces.add(16, 17, 18);
        mesh.faces.add(16, 18, 19);
        mesh.faces.add(20, 21, 22);
        mesh.faces.add(20, 22, 23);

        mesh.allocate();
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }
}
