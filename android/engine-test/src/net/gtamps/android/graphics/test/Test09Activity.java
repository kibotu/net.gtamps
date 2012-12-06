package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test12Action;
import net.gtamps.android.graphics.test.scenes.Test09Scene;
import net.gtamps.android.input.controller.InputEngineController;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 17:57
 */
public class Test09Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test12Action(new Test09Scene());
        setRenderAction(renderAction);

        // add input
        view.setOnTouchListener(InputEngineController.INSTANCE);
        view.setOnKeyListener(InputEngineController.INSTANCE);
    }
}
