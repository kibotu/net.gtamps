package net.gtamps.android.core.renderer;

import android.opengl.GLES20;
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

    @Override
    public void draw(GL10 gl10) {

        // clear screen
        gl10.glClearColor(.0f, .0f, .0f, 1.0f);
        gl10.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        // draw
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            BasicScene scene = renderActivity.getScenes().get(i);
            if(scene.isDirty()) scene.onDirty();
            scene.getScene().render(glState);
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
