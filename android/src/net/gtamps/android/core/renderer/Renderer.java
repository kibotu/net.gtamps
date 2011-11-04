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

import static android.opengl.GLES20.*;
import static javax.microedition.khronos.opengles.GL10.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.microedition.khronos.opengles.GL10.GL_SMOOTH;

public class Renderer implements GLSurfaceView.Renderer {

    private DefaultRenderActivity.IRenderActivity renderActivity;
    private ProcessingState glState;

    private ArrayList<BasicScene> basicScenes;
    private ConcurrentLinkedQueue<SceneNode> runtimeSetupQueue;

    public Renderer(DefaultRenderActivity.IRenderActivity renderActivity) {
        this.renderActivity = renderActivity;
        runtimeSetupQueue = new ConcurrentLinkedQueue<SceneNode>();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

//        GLES11 gles =  (GLES11) gl10;
//        if(gles == null) {
//            throw new ClassCastException("no opengles11");
//        }


        Logger.I(this, "Surface created.");
        Registry.setTextureLibrary(new TextureLibrary(gl10));

        // get mobile capabilities
        RenderCapabilities.setRenderCaps(gl10);

        // init game
        renderActivity.onCreate();
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).onCreate();
        }

        // default opengl settings
        reset(gl10);

        // finish basicScenes graph setup
        ProcessingState state = new ProcessingState();
        state.setGl(gl10);
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getScene().setup(state);
        }
        // last best gc call
        final Runtime r = Runtime.getRuntime();
        r.gc();

        // log how much memory is available
        Utils.logAvailableMemory();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Logger.i(this, "Surface changed.");
        //Prevent A Divide By Zero By : Making Height Equal One
        height = height == 0 ? 1 : height;
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getActiveCamera().setViewport(0, 0, width, height);
        }
    }

    private long startTime = SystemClock.elapsedRealtime();
    private int secondLength = 0;
    private int currentFps = 0;

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!renderActivity.isRunning() || renderActivity.isPaused()) {
            return;
        }
        renderActivity.onDrawFrame();

        gl10.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        // Compute elapsed time
        final long finalDelta = SystemClock.elapsedRealtime() - startTime;

        // new start time
        startTime = SystemClock.elapsedRealtime();

        // Limit Frame Rate
        if (Config.ENABLE_FRAME_LIMITER) {
            if (finalDelta < Config.FPS) {
                try {
                    Thread.sleep(Config.FPS - finalDelta);
                } catch (final InterruptedException e) {
                    Logger.printException(this, e);
                }
            }
        }

        // Display Current Frame Rate
        if (Config.DISPLAY_FRAME_RATE) {
            secondLength += finalDelta;
            currentFps++;
            if (secondLength > 1000) {
                Logger.i(this, "FPS: " + currentFps);
                secondLength = 0;
                currentFps = 0;
            }
        }

        // draw
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getScene().render(gl10);
        }

        // setup
        for (int i = 0; i < runtimeSetupQueue.size(); i++) {
            glState.setGl(gl10);
            runtimeSetupQueue.poll().setup(glState);
        }
    }

    public void addToSetupQueue(@NotNull SceneNode node) {
        runtimeSetupQueue.add(node);
    }

    private void reset(GL10 gl10) {

        glState = new ProcessingState();

        // Do OpenGL settings which we are using as defaults, or which we will not be changing on-draw

        // Explicit depth settings
        gl10.glEnable(GL_DITHER);                // Enable dithering
        gl10.glEnable(GL_DEPTH_TEST);            // Enables Depth Testing
        gl10.glClearDepthf(1.0f);                     // Depth Buffer Setup
        gl10.glDepthFunc(GL_LEQUAL);
        gl10.glDepthRangef(0, 1f);
        gl10.glDepthMask(true);

        gl10.glShadeModel(GL_SMOOTH);             // Enable Smooth Shading
        gl10.glEnable(GL10.GL_LIGHTING);

        // Alpha enabled
        gl10.glEnable(GL_BLEND);
        gl10.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl10.glEnable(GL11.GL_ALPHA_TEST);

        // kill alpha fragments
        gl10.glAlphaFunc(GL11.GL_GREATER, Config.ALPHA_KILL_FRAGMENTS_TOLERANCE);

//		// Texture
        gl10.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST_MIPMAP_NEAREST); // (OpenGL default is GL_NEAREST_MIPMAP)
        gl10.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); // (is OpenGL default)

        gl10.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl10.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // CCW frontfaces only, by default
        gl10.glFrontFace(GL_CCW);
        gl10.glCullFace(GL_BACK);
        gl10.glEnable(GL_CULL_FACE);

        //Really Nice Perspective Calculations
//        gl10.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl10.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
}
