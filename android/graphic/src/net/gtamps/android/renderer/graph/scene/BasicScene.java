package net.gtamps.android.renderer.graph.scene;

import net.gtamps.android.renderer.Registry;
import net.gtamps.android.renderer.graph.SceneNode;
import net.gtamps.android.renderer.graph.scene.primitives.Camera;
import net.gtamps.shared.Utils.IDirty;
import net.gtamps.shared.Utils.math.Color4;
import org.jetbrains.annotations.NotNull;

public abstract class BasicScene implements IDirty {

    private final SceneGraph scene = new SceneGraph();
    private boolean isDirty = true;

    public abstract void onCreate();

    final public void setActiveCamera(@NotNull Camera camera) {
        scene.setActiveCamera(camera);
    }

    final public Camera getActiveCamera() {
        return scene.getActiveCamera();
    }

    public void add(@NotNull SceneNode node) {
        scene.add(node);
        // dirty hack to setup up during runtime; i'm grateful for suggestions though
        Registry.getRenderer().addToSetupQueue(node);
    }

    public void remove(@NotNull SceneNode node) {
        scene.remove(node);
    }

    public SceneGraph getScene() {
        return scene;
    }

    final public Color4 getBackground() {
        return scene.getBackground();
    }

    final public void setBackground(Color4 color) {
        scene.setBackground(color);
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setDirtyFlag() {
        isDirty = true;
    }

    @Override
    public void clearDirtyFlag() {
        isDirty = false;
    }
}
