package net.gtamps.android.core.renderer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ShaderRenderer implements GLSurfaceView.Renderer  {

    // shader constants
	private final int GOURAUD_SHADER = 0;
	private final int PHONG_SHADER = 1;
	private final int NORMALMAP_SHADER = 2;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // initialize shaders
//        try {
//            _shaders[GOURAUD_SHADER] = new Shader(vShaders[GOURAUD_SHADER], fShaders[GOURAUD_SHADER], mContext, false, 0); // gouraud
//            _shaders[PHONG_SHADER] = new Shader(vShaders[PHONG_SHADER], fShaders[PHONG_SHADER], mContext, false, 0); // phong
//            _shaders[NORMALMAP_SHADER] = new Shader(vShaders[NORMALMAP_SHADER], fShaders[NORMALMAP_SHADER], mContext, false, 0); // normal map
//        } catch (Exception e) {
//            Log.d("SHADER 0 SETUP", e.getLocalizedMessage());
//        }

        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
		GLES20.glClearDepthf(1.0f);
		GLES20.glDepthFunc( GLES20.GL_LEQUAL );
		GLES20.glDepthMask( true );

		// cull backface
		GLES20.glEnable( GLES20.GL_CULL_FACE );
		GLES20.glCullFace(GLES20.GL_BACK);

    }

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
//		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 0.5f, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
    }

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Logger.e(this, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
