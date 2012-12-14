package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test13Action;
import net.gtamps.android.graphics.test.scenes.Test13Scene;
import net.gtamps.android.input.controller.InputEngineController;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:31
 */
public class Test13Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test13Action(new Test13Scene());
        setRenderAction(renderAction);

        // add input
        view.setOnTouchListener(InputEngineController.INSTANCE);
        view.setOnKeyListener(InputEngineController.INSTANCE);
    }
}
