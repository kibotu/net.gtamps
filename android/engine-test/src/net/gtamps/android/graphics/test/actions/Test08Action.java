package net.gtamps.android.graphics.test.actions;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.Utils.math.Quaternion;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Test08Action extends RenderAction {

    public Test08Action(@NotNull SceneGraph scene) {
        super(scene);
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
        rootNode.getChild(1).setRotation(rot * 0.02f, rot * 0.02f, rot * 0.02f);
        rootNode.getChild(2).getRotation(true).setEulerAnglesFromQuaternion(quat.setEulerAngles(rot, rot, rot));
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
