package net.gtamps.android.core.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.core.renderer.graph.scene.primitives.Cube;
import net.gtamps.android.core.renderer.shader.Shader;
import net.gtamps.android.core.utils.OpenGLUtils;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;

public class ShaderRenderer extends BasicRenderer {

    public ShaderRenderer(BasicRenderActivity.IRenderActivity renderActivity) {
        super(renderActivity);
    }

    @Override
    public void draw(GL10 unusedGL) {
        if(!RenderCapabilities.supportsGLES20()) return;

        // clear screen
        GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        int program = Shader.Type.PHONG.shader.getProgram();

        // unbound last shader
        GLES20.glUseProgram(program);
        Logger.checkGlError(this,"glUseProgram");

        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "lightPosition"), 1, lightP, 0);
        Logger.checkGlError(this,"lightPosition");
//        GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, "lightDirection"), 1, lightDir, 0);
//        Logger.checkGlError(this,"lightDirection");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "lightColor"), 1, lightC, 0);
        Logger.checkGlError(this,"lightColor");

        // draw scenes
        for(int i = 0; i < renderActivity.getScenes().size(); i++) {
            renderActivity.getScenes().get(i).getScene().update(getDelta());
            renderActivity.getScenes().get(i).getScene().process(glState);
        }
    }

    // light variables
    // light variables
    float[] lightP = {0, 0, 10f, 1f};
    float[] lightC = {0.5f, 0.5f, 0.5f, 1f};
    float[] lightDir = {0f, 0f, -1f};

    @Override
    public void reset(GL10 unusedGL) {
        glState.setGl(unusedGL);
        Shader.load();

        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

        // cull backface
        GLES20.glDisable( GLES20.GL_CULL_FACE );
        GLES20.glCullFace(GLES20.GL_BACK);
    }
}
