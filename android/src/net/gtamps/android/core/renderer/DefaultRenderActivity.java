package net.gtamps.android.core.renderer;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.android.core.utils.AndroidLogger;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;

import java.util.ArrayList;

import static android.opengl.GLES20.*;

/**
 * @see <a href="http://developer.android.com/images/activity_lifecycle.png">Activity LifeCycle</a>
 */
public abstract class DefaultRenderActivity extends Activity {

    protected GLSurfaceView view;

    public interface IRenderActivity {
        public void onCreate();

        public void onDrawFrame();

        public boolean isRunning();

        public boolean isPaused();

        public ArrayList<BasicScene> getScenes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable log
        Logger.setLogger(AndroidLogger.INSTANCE);
        Logger.I(this, "Application created.");
    }

    protected void onCreateSetContentView() {
        setContentView(view);
    }

    protected void glSurfaceViewConfig() {
        // transparent: scene.background = color4.transparent
        // manifest theme:translucent
//        view.setEGLConfigChooser(8,8,8,8, 16, 0);
//	    view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        if (Config.LOG_LEVEL.compareTo(Logger.Level.DEBUG_CHECK_GL_ERROR) <= 0)
            view.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
        if (Config.LOG_LEVEL.compareTo(Logger.Level.DEBUG_LOG_GL_CALLS) <= 0)
            view.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.I(this, "Application started.");
    }

    @Override
    public void onResume() {
        super.onResume();
        view.onResume();
        Logger.I(this, "Application resumed.");
    }

    @Override
    public void onPause() {
        super.onPause();
        view.onPause();
        Logger.I(this, "Application paused.");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.I(this, "Application stopped.");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Logger.I(this, "Application restarted.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.I(this, "Application destroyed.");
    }

    public void setRenderContinuously(boolean isContinuously) {
        view.setRenderMode((Config.renderContinuously = isContinuously) ? GLSurfaceView.RENDERMODE_CONTINUOUSLY : GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GL_VERTEX_SHADER, vertexSource);
        int pixelShader = loadShader(GL_FRAGMENT_SHADER, fragmentSource);

        int program = glCreateProgram();
        if (program != 0) {
            glAttachShader(program, vertexShader);
//            checkGlError("glAttachShader");
            glAttachShader(program, pixelShader);
//            checkGlError("glAttachShader");
            glLinkProgram(program);
            int[] linkStatus = new int[1];
            glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Logger.e(this, "Could not link program: ");
                Logger.e(this, glGetProgramInfoLog(program));
                glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private int loadShader(int shaderType, String source) {
        int shader = glCreateShader(shaderType);
        if (shader != 0) {
            glShaderSource(shader, source);
            glCompileShader(shader);
            int[] compiled = new int[1];
            glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Logger.e(this, "Could not compile shader " + shaderType + ":");
                Logger.e(this, glGetShaderInfoLog(shader));
                glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }
}
