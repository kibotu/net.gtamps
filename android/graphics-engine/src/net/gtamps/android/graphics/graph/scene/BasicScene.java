package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.utils.Registry;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public abstract class BasicScene {

    private final SceneGraph scene = new SceneGraph();

    public void add(@NotNull SceneNode node) {
        scene.add(node);
        Registry.getRenderer().addToSetupQueue(node);
    }

    public void remove(@NotNull SceneNode node) {
        scene.remove(node);
    }

    final public void setActiveCamera(@NotNull Camera camera) {
        scene.setActiveCamera(camera);
    }

    final public Camera getActiveCamera() {
        return scene.getActiveCamera();
    }

    public SceneGraph getScene() {
        return scene;
    }

    public abstract void onSurfaceCreated(GL10 gl10);

    public abstract void onDrawFrame(GL10 gl10);

    public abstract void onResume(GL10 gl10);
}
