package net.gtamps.android.renderer;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import net.gtamps.android.renderer.graph.ProcessingState;
import net.gtamps.android.renderer.graph.SceneNode;
import net.gtamps.android.renderer.mesh.texture.TextureLibrary;
import net.gtamps.android.renderer.shader.Shader;
import net.gtamps.android.renderer.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BasicRenderer implements GLSurfaceView.Renderer {

    protected IRenderAction renderAction;
    protected ProcessingState glState;

    private ConcurrentLinkedQueue<SceneNode> runtimeSetupQueue;

    public BasicRenderer(IRenderAction renderAction) {
        Logger.I(this, "Using " + this.getClass().getSimpleName() + ".");
        this.renderAction = renderAction;
        runtimeSetupQueue = new ConcurrentLinkedQueue<SceneNode>();
        glState = new ProcessingState();
    }

    @Override
    final public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Logger.I(this, "Surface created.");

        // get opengl context
        glState = new ProcessingState(gl10);

        Registry.setTextureLibrary(new TextureLibrary(gl10));

        // get mobile capabilities
        RenderCapabilities.setRenderCaps(gl10);

        // activity on create
        renderAction.onCreate();
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            renderAction.getScenes().get(i).onCreate();
        }

        // last best gc call
        final Runtime r = Runtime.getRuntime();
        r.gc();

        // log how much memory is available
        Utils.logAvailableMemory();

        reset();
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer
    }

    protected abstract void reset();

    @Override
    final public void onDrawFrame(GL10 gl10) {
        if (!renderAction.isRunning() || renderAction.isPaused()) return;

        // setup on the fly
        for (int i = 0; i < runtimeSetupQueue.size(); i++) {
            runtimeSetupQueue.poll().setup(glState);
        }

        // get time difference since last frame
        int delta = getDelta();

        // limits frame rate
        limitFrameRate(delta);

        // activity draw loop
        for(int i = 0; i < renderAction.getScenes().size(); i++) {
            if(renderAction.getScenes().get(i).isDirty()) renderAction.getScenes().get(i).onDirty();
        }
        renderAction.onDrawFrame();

        // render draw loop
        draw(gl10);

        // update real fps
        updateFPS();
    }

    public abstract void draw(GL10 unusedGL);

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Logger.i(this, "Surface changed.");
        height = height == 0 ? 1 : height;

        // re-allocate and re-validate texture
        Registry.getTextureLibrary().invalidate();

        // reload shader
        if(RenderCapabilities.supportsGLES20()) Shader.load();

        // inform camera that surface has changed
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            renderAction.getScenes().get(i).getScene().getActiveCamera().onSurfaceChanged(gl10, 0, 0, width, height);

            // re-allocate and re-validate hardware buffers
            renderAction.getScenes().get(i).getScene().onResume(glState);

            renderAction.getScenes().get(i).onDirty();
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
     * Deletes a texture by id.
     *
     * @param gl
     */
    public abstract void deleteTexture(int... textureIds);

    public abstract void clearScreen(Color4 bgcolor);
}
