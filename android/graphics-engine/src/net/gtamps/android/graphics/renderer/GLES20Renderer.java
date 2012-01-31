package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;
import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:30
 */
public class GLES20Renderer extends BasicRenderer {

    public GLES20Renderer(IRenderAction renderAction) {
        super(renderAction);
    }

    float[] lightC = {0.5f, 0.5f, 0.5f, 1f};

    @Override
    public void onDrawFrameHook(GL10 unusedGL) {
        if (!RenderCapabilities.supportsGLES20()) return;

        //int program = Shader.Type.PHONG.shader.getProgram();
        int program = 0;

        // unbound last shader
        GLES20.glUseProgram(program);
        Logger.checkGlError(this, "glUseProgram");

        GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, "lightPosition"), 1, renderAction.getScenes().get(0).getActiveCamera().getPosition().asArray(), 0);
        Logger.checkGlError(this, "lightPosition");
//        GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, "lightDirection"), 1, lightDir, 0);
//        Logger.checkGlError(this,"lightDirection");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "lightColor"), 1, lightC, 0);
        Logger.checkGlError(this, "lightColor");
    }

    @Override
    protected void onSurfaceCreatedHook(GL10 gl10, EGLConfig eglConfig) {
        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        // cull backface
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
    }

    @Override
    public int allocTexture(Bitmap texture, boolean generateMipMap) {
        int bitmapFormat = texture.getConfig() == Bitmap.Config.ARGB_8888 ? GLES20.GL_RGBA : GLES20.GL_RGB;
        int textureId = newTextureID();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapFormat, texture, 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, generateMipMap ? GLES20.GL_NEAREST_MIPMAP_NEAREST : GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        Logger.i(this, "[w:" + texture.getWidth() + "|h:" + texture.getHeight() + "|id:" + textureId + "|hasMipMap=" + generateMipMap + "] Bitmap atlas successfully allocated.");
        texture.recycle();
        return textureId;
    }

    @Override
    public int newTextureID() {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        return textureIds[0];
    }

    @Override
    public void deleteTexture(int... textureIds) {
        GLES20.glDeleteTextures(textureIds.length, textureIds, 0);
    }

    @Override
    public void clearScreen(Color4 bgcolor) {
        GLES20.glClearColor(bgcolor.r,bgcolor.g,bgcolor.b,bgcolor.a);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
        GLES20.glViewport(x,y,width,height);
        Logger.checkGlError(this, "glViewPort");
    }

    @Override
    public void applyCamera(Frustum frustum) {
//        int program = Shader.Type.PHONG.shader.getProgram();
        int program = 0;

        if (frustum.hasDepthTest()) GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        else GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        Matrix4 projectionMatrix = frustum.getProjectionMatrix();
        Matrix4 viewMatrix = frustum.getViewMatrix();
        Matrix4 normalMatrix = frustum.getNormalMatrix();
        Vector3 pos = frustum.getPosition();
        Vector3 target = frustum.getTarget();
        Vector3 up = frustum.getUp();

        Matrix4.setLookAt(viewMatrix, pos, target, up);
//         Matrix.setLookAtM(viewMatrix.values,0,pos.x,pos.y,pos.z,target.x,target.y,target.z,up.x,up.y,up.z);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "projectionMatrix"), 1, false, projectionMatrix.values, 0);
        Logger.checkGlError(this, "projectionMatrix");

        // Create the normal modelview matrix
        // Invert + transpose of mvpmatrix
//        Matrix.invertM(normalMatrix.values, 0, projectionMatrix.values, 0);
//        Matrix.transposeM(normalMatrix.values, 0, normalMatrix.values, 0);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "normalMatrix"), 1, false, normalMatrix.values, 0);
        Logger.checkGlError(this, "normalMatrix");

        // eye view matrix
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "viewMatrix"), 1, false, viewMatrix.values, 0);
        Logger.checkGlError(this, "viewMatrix");
    }
}
