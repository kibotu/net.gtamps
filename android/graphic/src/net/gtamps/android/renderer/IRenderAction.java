package net.gtamps.android.renderer;

import net.gtamps.android.renderer.graph.scene.BasicScene;

import java.util.ArrayList;

/**
 * Defines an life cycle for an activity.
 */
interface IRenderAction {

    /**
     * Called when renderer is created.
     */
    public void onCreate();

    /**
     * Called every frame.
     */
    public void onDrawFrame();

    /**
     * Method returns if the game is running.
     *
     * @return <code>true</code> if is running.
     */
    public boolean isRunning();

    /**
     * Method returns if the game is paused.
     *
     * @return <code>true</code> if is paused.
     */
    public boolean isPaused();

    public ArrayList<BasicScene> getScenes();

    public void stop();

    public void pause();

    public void resume();

    void start();
}