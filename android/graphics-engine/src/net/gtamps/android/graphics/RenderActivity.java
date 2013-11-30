package net.gtamps.android.graphics;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import net.gtamps.android.graphics.renderer.*;
import net.gtamps.android.graphics.renderer.RenderCapabilities;
import net.gtamps.android.graphics.utils.EGLConfigChooser;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.android.utils.AndroidLogger;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;

public abstract class RenderActivity extends Activity {

    protected GLSurfaceView view;
    protected IRenderAction renderAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable log
        Logger.setLogger(AndroidLogger.INSTANCE);
        Logger.I(this, "Application created.");
    }

    protected void setRenderAction(IRenderAction renderAction) {

        view = new GLSurfaceView(this);
        this.renderAction = renderAction;

        BasicRenderer renderer;

        // detect if OpenGL ES 2.0 support exists
        if (RenderCapabilities.detectOpenGLES20(this)) {
            view.setEGLContextClientVersion(2);
            renderer = new GLES20Renderer(renderAction);
        } else {
            renderer = new GL10Renderer(renderAction);
        }

        // set view render configurations
        glSurfaceViewConfig();
        view.setRenderer(renderer);
        setContentView(view);
        setRenderContinuously(true);

        // touchable
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setKeepScreenOn(true);

        // add as global variable
        Registry.setContext(this);
        Registry.setRenderer(renderer);
    }

    protected void glSurfaceViewConfig() {

        // transparent: scene.background = color4.transparent
        // manifest theme:translucent
        view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        view.setEGLConfigChooser(new EGLConfigChooser());

        view.getHolder().setFormat(PixelFormat.RGBA_8888);

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
        finish();
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

        if (renderAction != null) renderAction.stop();

        super.onDestroy();
        Logger.I(this, "Application destroyed.");
    }

    public void setRenderContinuously(boolean isContinuously) {
        view.setRenderMode((Config.renderContinuously = isContinuously) ? GLSurfaceView.RENDERMODE_CONTINUOUSLY : GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return renderAction.onCreateOptionsMenuHook(getMenuInflater(),menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final boolean option = renderAction.onOptionsItemSelectedHook(item);
        return option ? option : super.onOptionsItemSelected(item);
    }
}
