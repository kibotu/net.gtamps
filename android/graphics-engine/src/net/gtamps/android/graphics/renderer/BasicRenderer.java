package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.SceneNode;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vbo;
import net.gtamps.android.graphics.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:30
 */
public abstract class BasicRenderer implements GLSurfaceView.Renderer {

    protected IRenderAction renderAction;
    private final ConcurrentLinkedQueue<SceneNode> runtimeSetupQueue;

    public BasicRenderer(IRenderAction renderAction) {
        Logger.I(this, "Using " + this.getClass().getSimpleName() + ".");
        this.renderAction = renderAction;
        runtimeSetupQueue = new ConcurrentLinkedQueue<SceneNode>();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        // get mobile capabilities
        RenderCapabilities.setRenderCaps(gl10);

        Logger.I(this, "Surface created.");

        // activities on create
        renderAction.onSurfaceCreated(gl10);
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            renderAction.getScenes().get(i).onSurfaceCreated(gl10);
        }

        // last best gc call
        final Runtime r = Runtime.getRuntime();
        r.gc();

        // log how much memory is available
        Utils.logAvailableMemory();

        onSurfaceCreatedHook(gl10, eglConfig);
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Logger.I(this, "Surface changed.");
        height = height == 0 ? 1 : height;

        onSurfaceChangedHook(gl10, width, height);

        // inform camera that surface has changed
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            renderAction.getScenes().get(i).getActiveCamera().onSurfaceChanged(gl10, 0, 0, width, height);

            // re-allocate and re-validate hardware buffers
            renderAction.getScenes().get(i).onResume(gl10);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!renderAction.isRunning() || renderAction.isPaused()) return;

        // setup on the fly
        for (int i = 0; i < runtimeSetupQueue.size(); i++) {
            runtimeSetupQueue.poll().onCreate(gl10);
        }

        // get time difference since last frame
        int delta = getDelta();

        // limits frame rate
        limitFrameRate(delta);

        // activities draw loop
        renderAction.onDrawFrame(gl10);
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            renderAction.getScenes().get(i).onDrawFrame(gl10);
        }

        // render draw loop
        onDrawFrameHook(gl10);

        // update real fps
        updateFPS();
    }

    /**
     * Runs a node's onCreate() method once in next render loop.
     *
     * @param node
     */
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

    /**
     * time at last frame
     */
    protected long lastFrame;

    /**
     * frames per second
     */
    private int fps;

    /**
     * last fps time
     */
    private long lastFPS;

    protected final int getDelta() {
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
            if (Config.DISPLAY_FRAME_RATE) Logger.i("FPS: ", fps);
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
            Logger.printException(this, e);
        }
    }

    /**
     * Call back method for onDrawFrame()
     *
     * @param unusedGL
     */
    public abstract void onDrawFrameHook(GL10 unusedGL);


    /**
     * Call back method for onSurfaceCreated()
     *
     * @param gl10
     * @param eglConfig
     */
    protected abstract void onSurfaceCreatedHook(GL10 gl10, EGLConfig eglConfig);


    /**
     * Call back method for onSurfaceChanged()
     *
     * @param gl10
     * @param width
     * @param height
     */
    protected abstract void onSurfaceChangedHook(GL10 gl10, int width, int height);

    /**
     * Uploads a texture to hardware.
     *
     * @param texture
     * @param generateMipMap
     * @return textureId
     */
    public abstract int allocTexture(Bitmap texture, boolean generateMipMap);

    /**
     * Returns a new generated valid Texture id.
     *
     * @param gl
     * @return generatedId
     */
    public abstract int newTextureID();

    /**
     * Deletes textures by id.
     *
     * @param gl
     */
    public abstract void deleteTexture(int... textureIds);

    /**
     * Clears screen and sets background color.
     *
     * @param bgcolor
     */
    public abstract void clearScreen(Color4 bgcolor);

    /**
     * Sets viewport.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public abstract void setViewPort(int x, int y, int width, int height);

    /**
     * Applies camera.
     *
     * @param frustum
     */
    public abstract void applyCamera(Frustum frustum);

    /**
     * Actually draws a node.
     *
     * @param node
     */
    public abstract void draw(RenderableNode node);

    /**
     * Allocates hardwarebuffer.
     *
     * @param vertexBuffer
     * @param normalBuffer
     * @param colorBuffer
     * @param uvBuffer
     * @param indexBuffer
     *
     * @return allocated vbo
     */
    public abstract Vbo allocBuffers(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer colorBuffer, FloatBuffer uvBuffer, ShortBuffer indexBuffer);
}
