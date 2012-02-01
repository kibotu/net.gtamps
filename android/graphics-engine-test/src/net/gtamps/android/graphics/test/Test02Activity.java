package net.gtamps.android.graphics.test;

import android.os.Bundle;
import net.gtamps.android.graphics.RenderActivity;
import net.gtamps.android.graphics.test.actions.Test02Action;
import net.gtamps.android.graphics.test.scenes.Test02Scene;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 17:57
 */
public class Test02Activity extends RenderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new Test02Action(new Test02Scene());
        setRenderAction(renderAction);
    }
}
