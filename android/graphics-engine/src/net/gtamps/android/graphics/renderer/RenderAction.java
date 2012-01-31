package net.gtamps.android.graphics.renderer;

import android.os.SystemClock;
import net.gtamps.android.graphics.graph.scene.BasicScene;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

public abstract class RenderAction implements IRenderAction {

    protected boolean isRunning;
    protected boolean isPaused;
    protected final ArrayList<BasicScene> scenes;
    protected long startTime;

    public RenderAction() {
        scenes = new ArrayList<BasicScene>();
        start();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public ArrayList<BasicScene> getScenes() {
        return scenes;
    }

    @Override
    public void stop() {
        isRunning = false;
        isPaused = true;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void start() {
        isRunning = true;
        startTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!isRunning || isPaused) {
            return;
        }

        onDrawFrameHook(gl10);
    }

    protected abstract void onDrawFrameHook(GL10 gl10);
}
