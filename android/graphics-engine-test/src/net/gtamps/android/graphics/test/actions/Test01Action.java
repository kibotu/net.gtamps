package net.gtamps.android.graphics.test.actions;

import net.gtamps.android.graphics.graph.scene.BasicScene;
import net.gtamps.android.graphics.renderer.RenderAction;

public class Test01Action extends RenderAction {

    protected BasicScene world;

    public Test01Action(BasicScene scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }
    }
}
