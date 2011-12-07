package net.gtamps.android.core.renderer;

import android.os.SystemClock;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import java.util.logging.LoggingMXBean;

import static android.opengl.GLES20.*;
import static javax.microedition.khronos.opengles.GL10.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.microedition.khronos.opengles.GL10.GL_SMOOTH;

public class GLRenderer extends BasicRenderer {

    public GLRenderer(BasicRenderActivity.IRenderActivity renderActivity) {
       super(renderActivity);
    }

//    private long startTime = SystemClock.elapsedRealtime();
//    private int secondLength = 0;
//    private int currentFps = 0;

    @Override
    public void draw(GL10 gl10) {

//        // Compute elapsed time
//        final long finalDelta = SystemClock.elapsedRealtime() - startTime;
//
//        // new start time
//        startTime = SystemClock.elapsedRealtime();
//
//        // Limit Frame Rate
//        if (Config.ENABLE_FRAME_LIMITER) {
//            if (finalDelta < Config.FPS) {
//                try {
//                    Thread.sleep(Config.FPS - finalDelta);
//                } catch (final InterruptedException e) {
//                    Logger.printException(this, e);
//                }
//            }
//        }
//
//        // Display Current Frame Rate
//        if (Config.DISPLAY_FRAME_RATE) {
//            secondLength += finalDelta;
//            currentFps++;
//            if (secondLength > 1000) {
//                Logger.i(this, "FPS: " + currentFps);
//                secondLength = 0;
//                currentFps = 0;
//            }
//        }

        // draw
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            BasicScene scene = renderActivity.getScenes().get(i);
            if(scene.isDirty()) scene.onDirty();
            scene.getScene().render(gl10);
        }
    }

    @Override
    public void reset(GL10 gl10) {

        glState = new ProcessingState(gl10);

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
