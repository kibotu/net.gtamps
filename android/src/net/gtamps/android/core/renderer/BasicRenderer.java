package net.gtamps.android.core.renderer;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.android.core.renderer.mesh.texture.TextureLibrary;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;

public abstract class BasicRenderer implements GLSurfaceView.Renderer{

    protected BasicRenderActivity.IRenderActivity renderActivity;
    protected ProcessingState glState;

    protected ArrayList<BasicScene> basicScenes;
    private ConcurrentLinkedQueue<SceneNode> runtimeSetupQueue;

    public BasicRenderer(BasicRenderActivity.IRenderActivity renderActivity) {
        Logger.I(this, "Using " + this.getClass().getSimpleName() + ".");
        this.renderActivity = renderActivity;
        runtimeSetupQueue = new ConcurrentLinkedQueue<SceneNode>();
    }

    @Override
    final public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Logger.I(this, "Surface created.");
        Registry.setTextureLibrary(new TextureLibrary(gl10));

        // get mobile capabilities
        RenderCapabilities.setRenderCaps(gl10);

        // on create for all render activities
        renderActivity.onCreate();
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).onCreate();
        }

        // finish basicScenes graph setup
        glState = new ProcessingState(gl10);
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getScene().setup(glState);
        }

        // last best gc call
        final Runtime r = Runtime.getRuntime();
        r.gc();

        // log how much memory is available
        Utils.logAvailableMemory();

        reset(gl10);
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer
    }

    protected abstract void reset(GL10 gl10);

    @Override
    final public void onDrawFrame(GL10 gl10) {
        if (!renderActivity.isRunning() || renderActivity.isPaused())  return;

        // get time difference since last frame
        int delta = getDelta();

        limitFrameRate(delta);

        // activity draw loop
        renderActivity.onDrawFrame();

        // clear screen
        gl10.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        // custom implementation of a renderer
        draw(gl10);

        // setup on the fly
        for (int i = 0; i < runtimeSetupQueue.size(); i++) {
            glState.setGl(gl10);
            runtimeSetupQueue.poll().setup(glState);
        }

        // update real fps
        updateFPS();
    }

    public abstract void draw(GL10 unusedGL);

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Logger.i(this, "Surface changed.");
        height = height == 0 ? 1 : height;
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getActiveCamera().setViewport(0, 0, width, height);
        }
    }

    final public void addToSetupQueue(@NotNull SceneNode node) {
        runtimeSetupQueue.add(node);
    }

    /**
     * Get the time in milliseconds
     *
     * @return The system time in milliseconds
     */
    final public long getTime() {
        return SystemClock.elapsedRealtime();
    }

    /** time at last frame */
    private long lastFrame;

    /** frames per second */
    private int fps;

    /** last fps time */
    private long lastFPS;

    final private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    final private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            if(Config.DISPLAY_FRAME_RATE) Logger.i("FPS: ", fps);
            fps = 0; //reset the FPS counter
            lastFPS += 1000; //add one second
        }
        fps++;
    }

    /**
     * limit frame rate
     *
     * @param delta
     */
    private void limitFrameRate(int delta) {
        if (Config.ENABLE_FRAME_LIMITER && delta < Config.FPS) try {
            Thread.sleep(Config.FPS - delta);
        } catch (InterruptedException e) {
            Logger.printException(this,e);
        }
    }
}
