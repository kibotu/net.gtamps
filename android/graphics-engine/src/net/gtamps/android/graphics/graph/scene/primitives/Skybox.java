package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.Uv;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe
 * Date: 14/12/12
 * Time: 15:42
 */
public class Skybox extends RenderableNode {

    private Mesh mesh;

    public Skybox() {
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }


    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;
        mesh = new Mesh(12, 36);
        final float c = 1f;

        /** far **/
        // efg
        mesh.addVertex(Vector3.createNew(-c, -c, -c), Vector3.createNew(0, 0, c), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(c, -c, -c), Vector3.createNew(0, 0, c), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, c, -c), Vector3.createNew(0, 0, c), new Uv(c, 0));
        mesh.faces.add(0,1,2);
        // egh
        mesh.addVertex(Vector3.createNew(-c, -c, -c), Vector3.createNew(0, 0, c), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(c, c, -c), Vector3.createNew(0, 0, c), new Uv(c, 0));
        mesh.addVertex(Vector3.createNew(-c, c, -c), Vector3.createNew(0, 0, c), new Uv(0, 0));
        mesh.faces.add(3,4,5);

        /** near **/
        // abc
        mesh.addVertex(Vector3.createNew(-c, -c, c), Vector3.createNew(0, 0, -c), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, -c, c), Vector3.createNew(0, 0, -c), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(c, c, c), Vector3.createNew(0, 0, -c), new Uv(0, 0));
        mesh.faces.add(6,7,8);
        // acd
        mesh.addVertex(Vector3.createNew(-c, -c, c), Vector3.createNew(0, 0, -c), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, c, c), Vector3.createNew(0, 0, -c), new Uv(0, 0));
        mesh.addVertex(Vector3.createNew(-c, c, c), Vector3.createNew(0, 0, -c), new Uv(c, 0));
        mesh.faces.add(9,10,11);

        /** bottom **/
        // abf
        mesh.addVertex(Vector3.createNew(-c, -c, c), Vector3.createNew(0, c, 0), new Uv(c, 0));
        mesh.addVertex(Vector3.createNew(c, -c, c), Vector3.createNew(0, c, 0), new Uv(0, 0));
        mesh.addVertex(Vector3.createNew(c, -c, -c), Vector3.createNew(0, c, 0), new Uv(0, c));
        mesh.faces.add(12,13,14);
        // afe
        mesh.addVertex(Vector3.createNew(-c, -c, c), Vector3.createNew(0, c, 0), new Uv(c, 0));
        mesh.addVertex(Vector3.createNew(c, -c, -c), Vector3.createNew(0, c, 0), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(-c, -c, -c), Vector3.createNew(0, c, 0), new Uv(c, c));
        mesh.faces.add(15,16,17);

        /** top **/
        // dcg
        mesh.addVertex(Vector3.createNew(-c, c, c), Vector3.createNew(0, -c, 0), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, c, c), Vector3.createNew(0, -c, 0), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(c, c, -c), Vector3.createNew(0, -c, 0), new Uv(0, 0));
        mesh.faces.add(18,19,20);
        // dgh
        mesh.addVertex(Vector3.createNew(-c, c, c), Vector3.createNew(0, -c, 0), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, c, -c), Vector3.createNew(0, -c, 0), new Uv(0, 0));
        mesh.addVertex(Vector3.createNew(-c, c, -c), Vector3.createNew(0, -c, 0), new Uv(c, 0));
        mesh.faces.add(21,22,23);

        /** left **/
        // bfg
        mesh.addVertex(Vector3.createNew(c, -c, c), Vector3.createNew(-c, 0, 0), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, -c, -c), Vector3.createNew(-c, 0, 0), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(c, c, -c), Vector3.createNew(-c, 0, 0), new Uv(0, 0));
        mesh.faces.add(24,25,26);
        // bgc
        mesh.addVertex(Vector3.createNew(c, -c, c), Vector3.createNew(-c, 0, 0), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(c, c, -c), Vector3.createNew(-c, 0, 0), new Uv(0, 0));
        mesh.addVertex(Vector3.createNew(c, c, c), Vector3.createNew(-c, 0, 0), new Uv(c, 0));
        mesh.faces.add(27,28,29);

        /** right **/
        // aeh
        mesh.addVertex(Vector3.createNew(-c, -c, c), Vector3.createNew(c, 0, 0), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(-c, -c, -c), Vector3.createNew(c, 0, 0), new Uv(c, c));
        mesh.addVertex(Vector3.createNew(-c, c, -c), Vector3.createNew(c, 0, 0), new Uv(c, 0));
        mesh.faces.add(30,31,32);

        // ahd
        mesh.addVertex(Vector3.createNew(-c, -c, c), Vector3.createNew(c, 0, 0), new Uv(0, c));
        mesh.addVertex(Vector3.createNew(-c, c, -c), Vector3.createNew(c, 0, 0), new Uv(c, 0));
        mesh.addVertex(Vector3.createNew(-c, c, c), Vector3.createNew(c, 0, 0), new Uv(0, 0));
        mesh.faces.add(33,34,35);

        mesh.allocate();
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }
}
