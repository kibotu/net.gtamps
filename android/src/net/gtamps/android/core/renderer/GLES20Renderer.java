package net.gtamps.android.core.renderer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import net.gtamps.android.core.renderer.mesh.texture.TextureLibrary;
import net.gtamps.android.core.renderer.shader.Shader;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;
import java.util.List;

public class GLES20Renderer extends BasicRenderer {

    public GLES20Renderer(IRenderActivity renderActivity) {
        super(renderActivity);
    }

    @Override
    public void draw(GL10 unusedGL) {
        if (!RenderCapabilities.supportsGLES20()) return;

        // clear screen
        GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        int program = Shader.Type.PHONG.shader.getProgram();

        // unbound last shader
        GLES20.glUseProgram(program);
        Logger.checkGlError(this, "glUseProgram");

        GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, "lightPosition"), 1, renderActivity.getScenes().get(0).getActiveCamera().getPosition().asArray(), 0);
        Logger.checkGlError(this, "lightPosition");
//        GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, "lightDirection"), 1, lightDir, 0);
//        Logger.checkGlError(this,"lightDirection");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "lightColor"), 1, lightC, 0);
        Logger.checkGlError(this, "lightColor");

        // draw scenes
        for (int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getScene().update(getDelta());
            renderActivity.getScenes().get(i).getScene().process(glState);
        }
    }

    @Override
    public int allocTexture(Bitmap texture, boolean generateMipMap) {

        int textureId = newTextureID();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);

        Logger.i(this, "[w:" + texture.getWidth() + "|h:" + texture.getHeight() + "|id:" + textureId + "|hasMipMap=" + generateMipMap + "] Bitmap atlas successfully allocated.");

        texture.recycle();

        return textureId;
    }

    private void generateMipMap() {
        HashMap<String, Integer> map = Registry.getTextureLibrary().getTextureResourceIds();
        for(int i = 0; i < map.size(); i++){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, map.get(i));
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            Logger.checkGlError(this, "generate MipMap");
        }

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

    // light variables
    float[] lightP = {0, 0, 10f, 1f};
    float[] lightC = {0.5f, 0.5f, 0.5f, 1f};
    float[] lightDir = {0f, 0f, -1f};

    @Override
    public void reset() {
        Shader.load();

        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        // cull backface
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
    }
}
