package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.mesh.Mesh;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public abstract class RenderableNode extends GroupSceneNode {

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!isFrozen()) super.onDrawFrame(gl10);
        if (!isVisible()) return;


    }

    public abstract Mesh getMesh();
}
