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
public abstract class SceneGraph {

    private Camera activeCamera;
    private final GroupSceneNode rootNode = new GroupSceneNode();

    public SceneGraph(Camera camera) {
        setActiveCamera(camera);
    }

    final public void setActiveCamera(@NotNull Camera camera) {
        this.activeCamera = camera;
    }

    final public Camera getActiveCamera() {
        return activeCamera;
    }

    final public void remove(SceneNode node) {
        rootNode.remove(node);
    }

    final public void add(SceneNode node) {
        rootNode.add(node);
        Registry.getRenderer().addToSetupQueue(node);
    }

    public GroupSceneNode getRootNode() {
        return rootNode;
    }

    final public void onSurfaceCreated(GL10 gl10) {
        activeCamera.onCreate(gl10);
        onSurfaceCreatedInternal(gl10);
    }

    protected abstract void onSurfaceCreatedInternal(GL10 gl10);

    final public void onResume(GL10 gl10) {
        activeCamera.onResume(gl10);
        rootNode.onResume(gl10);
    }

    final public void onDrawFrame(GL10 gl10) {
        activeCamera.onDrawFrame(gl10);
        rootNode.onDrawFrame(gl10);
    }
}
