package net.gtamps.android.graphics.test.actions;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.renderer.RenderAction;

import javax.microedition.khronos.opengles.GL10;

public class Test01Action extends RenderAction {

    protected SceneGraph world;

    public Test01Action(SceneGraph scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    float rot = 0;
    
    @Override
    protected void onDrawFrameHook(GL10 gl10) {
        rot+=0.01;
        getScenes().get(0).getRootNode().getChild(1).setRotation(rot,rot,rot);
    }
}
