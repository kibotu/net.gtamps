package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.StaticMeshNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import org.jetbrains.annotations.NotNull;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public class Cube extends StaticMeshNode<Cube> {

    public Cube(@NotNull Mesh mesh) {
        super(mesh);
    }
    
    public Cube() {
        this(new Mesh());
    }
}
