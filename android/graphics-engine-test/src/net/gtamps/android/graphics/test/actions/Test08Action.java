package net.gtamps.android.graphics.test.actions;

import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.game.state.State;

import javax.microedition.khronos.opengles.GL10;

public class Test08Action extends RenderAction {

    protected SceneGraph world;

    public Test08Action(SceneGraph scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    float rot = 0;

    @Override
    protected void onDrawFrameHook(GL10 gl10) {
        rot += 0.03;
        RootNode rootNode = getScenes().get(0).getRootNode();
        for (int i = 0; i < rootNode.size(); i++) {
            rootNode.getChild(i).setRotation(rot, rot, rot);
        }
    }
}
