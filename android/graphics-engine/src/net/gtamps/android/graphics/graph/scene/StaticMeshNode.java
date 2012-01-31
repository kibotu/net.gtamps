package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import org.jetbrains.annotations.NotNull;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public abstract class StaticMeshNode<T extends RenderableNode> extends RenderableNode {

    public StaticMeshNode(@NotNull Mesh mesh) {
        super(mesh);
    }
}
