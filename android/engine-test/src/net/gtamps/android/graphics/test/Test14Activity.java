package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test14Action;
import net.gtamps.android.graphics.test.scenes.Test14Scene;
import net.gtamps.android.input.controller.InputEngineController;

public class Test14Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test14Action(new Test14Scene());
        setRenderAction(renderAction);

        // add input
        view.setOnTouchListener(InputEngineController.INSTANCE);
        view.setOnKeyListener(InputEngineController.INSTANCE);
    }
}
