package net.gtamps.android.gtandroid;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.gtandroid.actions.StartAction;
import net.gtamps.android.gtandroid.core.input.InputEngineController;
import net.gtamps.android.gtandroid.scenes.StartScene;

/**
 * User: Jan Rabe
 * Date: 17/12/12
 * Time: 09:47
 */
public class GTA3D extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // render action and scene
        renderAction = new StartAction(new StartScene());
        setRenderAction(renderAction);

        // input
        view.setOnTouchListener(InputEngineController.getInstance());
        view.setOnKeyListener(InputEngineController.getInstance());
    }
}
