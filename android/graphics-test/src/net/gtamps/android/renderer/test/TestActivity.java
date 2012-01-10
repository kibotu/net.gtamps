package net.gtamps.android.renderer.test;

import android.os.Bundle;
import net.gtamps.android.renderer.BasicRenderActivity;
import net.gtamps.android.renderer.RenderAction;
import net.gtamps.android.renderer.test.action.TestAction;

public class TestActivity extends BasicRenderActivity {

    private RenderAction renderAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderAction = new TestAction();
        setRenderAction(renderAction);
    }
}
