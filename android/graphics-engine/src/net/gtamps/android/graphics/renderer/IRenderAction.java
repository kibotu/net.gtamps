package net.gtamps.android.graphics.renderer;

import net.gtamps.android.graphics.graph.scene.SceneGraph;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
 * Defines an life cycle for an activities.
 */
public interface IRenderAction {

    /**
     * Called when renderer is created.
     *
     * @param gl10
     */
    void onSurfaceCreated(GL10 gl10);

    /**
     * Called every frame.
     *
     * @param gl10
     */
    public void onDrawFrame(GL10 gl10);

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
    public ArrayList<SceneGraph> getScenes();

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