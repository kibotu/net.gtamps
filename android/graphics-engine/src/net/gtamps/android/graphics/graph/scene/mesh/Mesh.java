package net.gtamps.android.graphics.graph.scene.mesh;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.shared.Utils.Logger;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:02
 */
public class Mesh<T extends RenderableNode> {

    public Mesh(int faces, int maxVertices) {
        Logger.I(this, "new Mesh(" + faces + ", " + maxVertices + ")");
    }
}
