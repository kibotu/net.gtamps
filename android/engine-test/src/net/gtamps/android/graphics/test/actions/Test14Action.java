package net.gtamps.android.graphics.test.actions;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Test14Action extends RenderAction {

    public Test14Action(@NotNull SceneGraph scene) {
        super(scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    private float rot = 0;
    private Vector3 cameraRotAngle = Vector3.createNew();

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        rot += 0.5;
        RootNode rootNode = getScenes().get(0).getRootNode();

        cameraRotAngle.set(rot, 0, 0);
        getScenes().get(0).getActiveCamera().rotateAroundVector(rootNode.getPosition(), cameraRotAngle, 10);
    }

    @Override
    public boolean onCreateOptionsMenuHook(MenuInflater menuInflater, Menu menu) {
//        menuInflater.inflate(R.menu.menu_14, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelectedHook(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return false;
        }
    }
}