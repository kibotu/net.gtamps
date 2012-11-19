package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.parser.ObjParser;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 14:04
 */
public class Object3D extends RenderableNode {

    private Mesh mesh;

    public Object3D(String objectResourceID) {
        this(objectResourceID, true);
    }

    public Object3D(String objectResourceID, boolean generateMipMaps) {
        ObjParser parser = new ObjParser(objectResourceID, generateMipMaps);
        parser.parse();
        parser.getParsedObject(this);
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        if (mesh != null) return;
        mesh.allocate();
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }
}
