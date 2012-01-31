package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public abstract class RenderableNode extends SceneNode {

    @Override
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);


    }

    public abstract Mesh getMesh();
}
