package net.gtamps.android;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.renderer.*;
import net.gtamps.android.game.Game;
import net.gtamps.shared.Config;

public class GTA extends BasicRenderActivity {

    public static final String TAG = GTA.class.getSimpleName();

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game();
        view = new GLSurfaceView(this);

        BasicRenderer renderer;

        // detect if OpenGL ES 2.0 support exists
//        if (RenderCapabilities.detectOpenGLES20(this)) {
        if (RenderCapabilities.supportsOpenGLES = false) {
            view.setEGLContextClientVersion(2);
            renderer = new GLES20Renderer(game);
        } else {
            renderer = new GL10Renderer(game);
        }

        // set view render configurations
        glSurfaceViewConfig();
        view.setRenderer(renderer);
        setContentView(view);
        setRenderContinuously(Config.renderContinuously);

        // touchable
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setKeepScreenOn(true);
        view.setOnTouchListener(InputEngineController.getInstance());
        view.setOnKeyListener(InputEngineController.getInstance());

        // add as global variable
        Registry.setContext(this);
        Registry.setRenderer(renderer);
    }

    @Override
    public void onDestroy() {
        game.stop();
        super.onDestroy();
    }
}
