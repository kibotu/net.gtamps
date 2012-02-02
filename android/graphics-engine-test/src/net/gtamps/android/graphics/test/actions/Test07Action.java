package net.gtamps.android.graphics.test.actions;

import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.SceneNode;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.game.state.State;

import javax.microedition.khronos.opengles.GL10;

public class Test07Action extends RenderAction {

    protected SceneGraph world;

    public Test07Action(SceneGraph scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    float increment = 0;

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        RootNode rootNode = getScenes().get(0).getRootNode();
        ((RenderableNode)rootNode.getChild(1)).animate(State.Type.IDLE, increment+=0.1f%1);
    }
}
