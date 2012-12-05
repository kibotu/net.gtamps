package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test11Action;
import net.gtamps.android.graphics.test.scenes.Test12Scene;
import net.gtamps.android.input.controller.InputEngineController;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:31
 */
public class Test12Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test11Action(new Test12Scene());
        setRenderAction(renderAction);

        // add input
        view.setOnTouchListener(InputEngineController.INSTANCE);
        view.setOnKeyListener(InputEngineController.INSTANCE);
    }
}
