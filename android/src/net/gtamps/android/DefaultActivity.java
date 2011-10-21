package net.gtamps.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import net.gtamps.android.core.utils.AndroidLogger;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;

/**
 * @see <a href="http://developer.android.com/images/activity_lifecycle.png">Activity LifeCycle</a>
 */
public abstract class DefaultActivity extends Activity {

    protected GLSurfaceView view;

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
//        view.setEGLConfigChooser(8,8,8,8, 16, 0);
//	    view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        // opengl debugging
//        view.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
//        view.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.log(TAG, "\n\n\n\n\nApplication started.\n\n\n\n\n");
    }

    @Override
    public void onResume() {
        super.onResume();
        view.onResume();
        Utils.log(TAG, "\n\n\n\n\nApplication resumed.\n\n\n\n\n");
    }

    @Override
    public void onPause() {
        super.onPause();
        view.onPause();
        Utils.log(TAG, "\n\n\n\n\nApplication paused.\n\n\n\n\n");
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.log(TAG, "\n\n\n\n\nApplication stopped.\n\n\n\n\n");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Utils.log(TAG, "\n\n\n\n\nApplication restarted.\n\n\n\n\n");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.log(TAG, "\n\n\n\n\nApplication destroyed.\n\n\n\n\n");
    }

    public void setRenderContinuously(boolean isContinuously) {
//        view.setRenderMode((Config.renderContinuously = isContinuously) ? GLSurfaceView.RENDERMODE_CONTINUOUSLY : GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        view.setRenderMode((Config.renderContinuously = isContinuously) ? 1 : 0);
    }
}
