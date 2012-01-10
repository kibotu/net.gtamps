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

    float rot = 0;
    
    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        rot+=0.01f;
        for(int i = 0; i < scenes.get(0).getScene().getChildCount(); i++) {
            scenes.get(0).getScene().get(i).setRotation(rot,rot,rot);
        }
    }
}
