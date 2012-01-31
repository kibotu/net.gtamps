package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public abstract class RenderableNode<T extends SceneNode> extends SceneNode {

    protected static Mesh mesh;

    public RenderableNode(@NotNull Mesh mesh) {
        this.mesh = mesh;
    }

    public RenderableNode(int faces, int maxVertices) {
        this(getMesh() == null ? new Mesh(faces, maxVertices) : getMesh());
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
    }

    public final static <T> Mesh getMesh() {
        return mesh;
    }
}
