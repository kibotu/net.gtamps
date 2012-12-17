package net.gtamps.android.graphics.test.actions;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Test01Action extends RenderAction {

    public Test01Action(@NotNull SceneGraph scene) {
        super(scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    private float rot = 0;
    Quaternion quat = new Quaternion();
    private Vector3 cameraRotAngle = Vector3.createNew();

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        rot += 1;
        RootNode rootNode = getScenes().get(0).getRootNode();
        for (int i = 1; i < rootNode.size(); i++) {
            rootNode.getChild(i).getRotation(true).setEulerAnglesFromQuaternion(quat.setEulerAngles(rot, rot, rot));
        }

        cameraRotAngle.set(rot, 0, 0);
        getScenes().get(0).getActiveCamera().rotateAroundVector(rootNode.getPosition(), cameraRotAngle, 15);
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
