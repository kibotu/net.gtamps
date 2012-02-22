package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test01Action;
import net.gtamps.android.graphics.test.scenes.Test010Scene;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 17:57
 */
public class Test10Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test01Action(new Test010Scene());
        setRenderAction(renderAction);
    }
}
