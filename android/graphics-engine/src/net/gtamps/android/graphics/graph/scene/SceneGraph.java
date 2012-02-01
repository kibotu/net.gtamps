package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.utils.Registry;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:50
 */
public class SceneGraph {

    private Camera activeCamera;
    private GroupSceneNode group = new GroupSceneNode();

    public SceneGraph() {
    }

    public void setActiveCamera(@NotNull Camera camera) {
        this.activeCamera = camera;
    }

    public Camera getActiveCamera() {
        return activeCamera;
    }

    public void remove(SceneNode node) {
        group.remove(node);
    }

    public void add(SceneNode node) {
        group.add(node);
        Registry.getRenderer().addToSetupQueue(node);
    }

    public void onSurfaceCreated(GL10 gl10) {
        activeCamera.onCreate(gl10);
    }

    public void onResume(GL10 gl10) {
        group.onResume(gl10);
    }

    public void onDrawFrame(GL10 gl10) {
        group.onDrawFrame(gl10);
    }
}
