package net.gtamps.android.renderer.test;

import net.gtamps.android.renderer.RenderAction;
import net.gtamps.android.renderer.graph.scene.BasicScene;

public class TestAction extends RenderAction {

    protected BasicScene world;

    public TestAction() {
        world = new World();
    }

    @Override
    public void onCreate() {
        scenes.add(world);
    }

    float i = 0;
    
    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        i+=0.01f;
        scenes.get(0).getScene().get(0).setRotation(i,i,i);
    }
}
