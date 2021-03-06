package net.gtamps.android.renderer;

import android.graphics.Bitmap;
import android.opengl.GLU;
import android.opengl.GLUtils;
import net.gtamps.android.renderer.graph.scene.BasicScene;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.GL_PERSPECTIVE_CORRECTION_HINT;

public class GL10Renderer extends BasicRenderer {

    public GL10Renderer(IRenderAction renderAction) {
        super(renderAction);
    }

    @Override
    public void draw(GL10 gl10) {

        // draw
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            BasicScene scene = renderAction.getScenes().get(i);
            if (scene.isDirty()) scene.onDirty();
            scene.getScene().render(glState);
        }
    }

    @Override
    public int allocTexture(Bitmap texture, boolean generateMipMap) {

        GL11 gl = glState.getGl11();

        int glTextureId = newTextureID();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, glTextureId);

        if (generateMipMap && gl instanceof GL11) {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST_MIPMAP_NEAREST);
            gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        } else {
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_FALSE);
        }

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        // 'upload' to gpu
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);

        Logger.i(this, "[w:" + texture.getWidth() + "|h:" + texture.getHeight() + "|id:" + glTextureId + "|hasMipMap=" + generateMipMap + "] Bitmap atlas successfully allocated.");

        //Clean up
        texture.recycle();

        return glTextureId;
    }

    final int[] newTextureId = new int[1];

    @Override
    public int newTextureID() {
        glState.getGl().glGenTextures(1, newTextureId, 0);
        return newTextureId[0];
    }

    @Override
    public void deleteTexture(int... textureId) {
        glState.getGl().glDeleteTextures(textureId.length, textureId, 0);
    }

    @Override
    public void clearScreen(Color4 bgcolor) {
        GL10 gl10 = glState.getGl();
        gl10.glClearColor(bgcolor.r,bgcolor.g,bgcolor.b,bgcolor.a);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void applyCamera(Frustum frustum) {
        GL10 gl = glState.getGl();
        if(gl == null) return;

        if (frustum.hasDepthTest()) gl.glEnable(GL10.GL_DEPTH_TEST);
        else gl.glDisable(GL10.GL_DEPTH_TEST);

        Vector3 pos = frustum.getPosition();
        Vector3 target = frustum.getTarget();
        Vector3 up = frustum.getUp();

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(glState.getGl(), frustum.getHorizontalFieldOfView(), frustum.getAspectRatio(), frustum.getNearDistance(), frustum.getFarDistance());
        GLU.gluLookAt(gl, pos.x, pos.y, pos.z, target.x, target.y, target.z, up.x, up.y, up.z);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
        glState.getGl().glViewport(x, y, width, height);
    }

    @Override
    public void reset() {

        GL10 gl10 = glState.getGl();

        // Explicit depth settings
        gl10.glEnable(GL10.GL_DITHER);                // Enable dithering
        gl10.glEnable(GL10.GL_DEPTH_TEST);            // Enables Depth Testing
        gl10.glClearDepthf(1.0f);                     // Depth Buffer Setup
        gl10.glDepthFunc(GL10.GL_LEQUAL);
        gl10.glDepthRangef(0, 1f);
        gl10.glDepthMask(true);

        gl10.glShadeModel(GL10.GL_SMOOTH);             // Enable Smooth Shading
        gl10.glEnable(GL10.GL_LIGHTING);

        // Alpha enabled
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glEnable(GL11.GL_ALPHA_TEST);

        // kill alpha fragments
        gl10.glAlphaFunc(GL11.GL_GREATER, Config.ALPHA_KILL_FRAGMENTS_TOLERANCE);

//		// Texture
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST_MIPMAP_NEAREST); // (OpenGL default is GL_NEAREST_MIPMAP)
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR); // (is OpenGL default)

        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        // CCW frontfaces only, by default
        gl10.glFrontFace(GL10.GL_CCW);
        gl10.glCullFace(GL10.GL_BACK);
        gl10.glEnable(GL10.GL_CULL_FACE);

        //Really Nice Perspective Calculations
//        gl10.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl10.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }
}
