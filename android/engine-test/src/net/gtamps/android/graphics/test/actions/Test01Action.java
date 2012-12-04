package net.gtamps.android.graphics.test.actions;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.Utils.math.Quaternion;

import javax.microedition.khronos.opengles.GL10;

public class Test01Action extends RenderAction {

    protected SceneGraph world;

    public Test01Action(SceneGraph scene) {
        scenes.add(world = scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    private float rot = 0;
    Quaternion quat = new Quaternion();

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        rot += 1;
        RootNode rootNode = getScenes().get(0).getRootNode();
        for (int i = 1; i < rootNode.size(); i++) {
            rootNode.getChild(i).getRotation(true).setEulerAnglesFromQuaternion(quat.setEulerAngles(rot, rot, rot));
        }
    }

    @Override
    public boolean onCreateOptionsMenuHook(MenuInflater menuInflater, Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelectedHook(MenuItem item) {
        return false;
    }

}
