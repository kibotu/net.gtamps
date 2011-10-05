package net.gtamps.android;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.Renderer;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.game.Game;
import net.gtamps.shared.RandomSharedObject;

public class GTA extends DefaultActivity {

    public static final String TAG = GTA.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Game game = new Game();
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

        RandomSharedObject o = new RandomSharedObject();
        o.i = 50;
        Utils.log(TAG,""+o.i);

    }
}
