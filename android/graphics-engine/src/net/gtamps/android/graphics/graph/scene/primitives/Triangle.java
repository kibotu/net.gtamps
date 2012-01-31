package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.StaticMeshNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import org.jetbrains.annotations.NotNull;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:25
 */
public class Triangle extends StaticMeshNode<Triangle> {

    public Triangle(@NotNull Mesh mesh) {
        super(mesh);
    }

    public Triangle() {
        this(new Mesh());
    }
}
