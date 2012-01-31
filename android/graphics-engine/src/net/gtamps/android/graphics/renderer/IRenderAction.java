package net.gtamps.android.graphics.renderer;

import net.gtamps.android.graphics.graph.scene.BasicScene;

import java.util.ArrayList;

/**
 * Defines an life cycle for an activity.
 */
public interface IRenderAction {

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

    /**
     * Holds all scenes within the action.
     *
     * @return
     */
    public ArrayList<BasicScene> getScenes();

    /**
     * Stops the action.
     */
    public void stop();

    /**
     * Pauses the action.
     */
    public void pause();

    /**
     * Resumes the action.
     */
    public void resume();

    /**
     * Starts the action.
     */
    void start();
}