package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:50
 */
public class SceneGraph {

    private ArrayList<SceneNode> children;
    private Camera activeCamera;

    public SceneGraph() {
        children = new ArrayList<SceneNode>(50);
    }

    public void add(SceneNode node) {
        children.add(node);
    }

    public void setActiveCamera(@NotNull Camera camera) {
        this.activeCamera = camera;
    }

    public Camera getActiveCamera() {
        return activeCamera;
    }
}
