package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import net.gtamps.android.graphics.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:30
 */
public abstract class BasicRenderer implements GLSurfaceView.Renderer {

    private IRenderAction renderAction;

    public BasicRenderer(IRenderAction renderAction) {
        this.renderAction = renderAction;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        // get mobile capabilities
        RenderCapabilities.setRenderCaps(gl10);

        Logger.I(this, "Surface created.");

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
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!renderAction.isRunning() || renderAction.isPaused()) return;

        // get time difference since last frame
        int delta = getDelta();

        // limits frame rate
        limitFrameRate(delta);

        renderAction.onDrawFrame();

        // render draw loop
        onDrawFrameHook(gl10);

        // update real fps
        updateFPS();
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
}
