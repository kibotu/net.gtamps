package net.gtamps.android.graphics.test.actions;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.android.graphics.test.R;

import javax.microedition.khronos.opengles.GL10;

public class Test11Action extends RenderAction {

    protected SceneGraph world;

    public Test11Action(SceneGraph scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    private float rot = 0;

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        rot += 0.01;
        RootNode rootNode = getScenes().get(0).getRootNode();
        for (int i = 1; i < rootNode.size(); i++) {
            rootNode.getChild(i).setRotation(0, rot, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenuHook(MenuInflater menuInflater, Menu menu) {
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    private String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

    @Override
    public boolean onOptionsItemSelectedHook(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.click:
                AnimatedSkeletonObject3D obj = (AnimatedSkeletonObject3D) getScenes().get(0).getRootNode().getChild(1);
                if (obj != null) {
                    obj.play(PACKAGE_NAME + "katarina_idle1_anm");
                }
                return true;
            default:
                return false;
        }
    }
}
