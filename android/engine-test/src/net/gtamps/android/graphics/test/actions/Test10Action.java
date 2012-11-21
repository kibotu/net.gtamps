package net.gtamps.android.graphics.test.actions;

import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.AnimationObject3D;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;

public class Test10Action extends RenderAction {

    protected SceneGraph world;

    public Test10Action(SceneGraph scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    float rot = 0;

    private long endTime;
    private long startTime;

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        rot += 0.01;
        RootNode rootNode = getScenes().get(0).getRootNode();
        for (int i = 0; i < rootNode.size(); i++) {
            rootNode.getChild(i).setRotation(rot, rot, rot);
//            rootNode.setRotation(rot,rot,rot);
        }

        ((AnimationObject3D) rootNode.getChild(1)).playFrame("shapeshift", "shapeshift01");
    }
}
