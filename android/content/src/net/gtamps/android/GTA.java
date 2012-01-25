package net.gtamps.android;

import android.os.Bundle;
import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.game.Game;
import net.gtamps.android.game.content.scenes.inputlistener.PlayerMovementListener;
import net.gtamps.android.renderer.*;

public class GTA extends BasicRenderActivity {

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game();
        setRenderAction(game);

        view.setOnTouchListener(InputEngineController.getInstance());
        view.setOnKeyListener(InputEngineController.getInstance());
        
    }
}
