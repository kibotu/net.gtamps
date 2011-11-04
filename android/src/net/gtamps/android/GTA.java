package net.gtamps.android;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.BasicRenderActivity;
import net.gtamps.android.core.renderer.Registry;
import net.gtamps.android.core.renderer.Renderer;
import net.gtamps.android.game.Game;

public class GTA extends BasicRenderActivity {

    public static final String TAG = GTA.class.getSimpleName();

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game();
        Registry.setContext(this);
        Renderer renderer = new Renderer(game);
        Registry.setRenderer(renderer);

        view = new GLSurfaceView(this);
        glSurfaceViewConfig();
        view.setRenderer(renderer);
        setRenderContinuously(true);
        onCreateSetContentView();

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setKeepScreenOn(true);

        // !!!!!!! took me ages to find, but there are smooth images available now ^_^
        view.getHolder().setFormat(PixelFormat.RGBA_8888);

        view.setOnTouchListener(InputEngine.getInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        game.stop();
    }
}
