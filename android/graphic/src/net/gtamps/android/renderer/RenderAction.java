package net.gtamps.android.renderer;

import net.gtamps.android.renderer.graph.scene.BasicScene;

import java.util.ArrayList;

public abstract class RenderAction implements IRenderAction {

    protected boolean isRunning;
    protected boolean isPaused;
    protected final ArrayList<BasicScene> scenes;

    public RenderAction() {
        scenes = new ArrayList<BasicScene>();
        isRunning = true;
        isPaused = false;
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
}
