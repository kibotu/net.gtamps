package net.gtamps.android.graphics.test.actions;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.rigged.RiggedObject3D;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.renderer.RenderAction;
import net.gtamps.android.graphics.test.R;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.UIDGenerator;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.util.Calendar;

public class Test11Action extends RenderAction {

    public Test11Action(@NotNull SceneGraph scene) {
        super(scene);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10) {
    }

    private float rot = 0;
    private Vector3 cameraRotAngle = Vector3.createNew();

    @Override
    protected void onDrawFrameHook(GL10 gl10) {

        rot += 1;
        RootNode rootNode = getScenes().get(0).getRootNode();

        cameraRotAngle.set(rot, 0, 0);
        getScenes().get(0).getActiveCamera().rotateAroundVector(rootNode.getPosition(), cameraRotAngle, 5);
//        captureVideo(360, 0);
    }

    private int counter = 360;
    private long captureStartTime = Calendar.getInstance().getTimeInMillis();
    private void captureVideo(int amountScreens, int interval) {

        if(amountScreens < counter) return;

        // find out if enough time has passed
        int dtInterpolation = (int) (interval - (System.currentTimeMillis() - captureStartTime));

        // update
        if (dtInterpolation <= 0) {
            Registry.getRenderer().captureScreenshot(Environment.getExternalStorageDirectory().toString()+"/aa_gtamps_screens/","   screenshot_"+counter+".png");
            ++counter;
        } else {
            captureStartTime = Calendar.getInstance().getTimeInMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenuHook(MenuInflater menuInflater, Menu menu) {
        menuInflater.inflate(R.menu.menu_11, menu);
        return true;
    }

    private String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";
    private boolean showSkn = true;

    @Override
    public boolean onOptionsItemSelectedHook(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.animate:
                AnimatedSkeletonObject3D obj = (AnimatedSkeletonObject3D) getScenes().get(0).getRootNode().getChild(1);
                if (obj != null) {
                    obj.play(PACKAGE_NAME + "katarina_dance_anm");
                }
                return true;
            case R.id.show_bones:
                showSkn = !showSkn;
                getScenes().get(0).getRootNode().getChild(1).setVisible(showSkn);  // skn
                getScenes().get(0).getRootNode().getChild(2).setVisible(!showSkn); // skl
                return true;
            case R.id.capture_screen:
                Registry.getRenderer().captureScreenshot(Environment.getExternalStorageDirectory().toString()+"/aa_gtamps_screens/", "screenshot_"+UIDGenerator.getNewUID()+".png");
                return true;
            case R.id.capture_video:
                counter = 0;
                return true;
            default:
                return false;
        }
    }
}