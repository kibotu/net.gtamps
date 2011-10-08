package net.gtamps.android;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.Renderer;
import net.gtamps.android.game.Game;

public class GTA extends DefaultActivity {

    public static final String TAG = GTA.class.getSimpleName();

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game();
        Registry.setContext(this);
        Renderer renderer = new Renderer(game);

        view = new GLSurfaceView(this);
        glSurfaceViewConfig();
        view.setRenderer(renderer);
		setRenderContinuously(true);
        onCreateSetContentView();

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setKeepScreenOn(true);

        view.setOnTouchListener(InputEngine.getInstance());
    }

    public void onDestroy() {
        game.stop();
    }
}
