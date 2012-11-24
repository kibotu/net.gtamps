package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test10Action;
import net.gtamps.android.graphics.test.actions.Test11Action;
import net.gtamps.android.graphics.test.scenes.Test10Scene;
import net.gtamps.android.graphics.test.scenes.Test11Scene;
import net.gtamps.android.input.controller.InputEngineController;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 17:57
 */
public class Test11Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test11Action(new Test11Scene());
        setRenderAction(renderAction);

        // add input
        view.setOnTouchListener(InputEngineController.INSTANCE);
        view.setOnKeyListener(InputEngineController.INSTANCE);
    }
}
