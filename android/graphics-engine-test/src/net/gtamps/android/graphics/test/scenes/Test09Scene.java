package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.test.input.layout.CameraInputInterpreter;
import net.gtamps.android.graphics.test.input.layout.DefaultLayout;
import net.gtamps.android.input.controller.InputEngineController;
import net.gtamps.android.input.view.TouchInputButton;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test09Scene extends SceneGraph {

    public Test09Scene() {
        super(new Camera(0, 0, 15, 0, 0, -1, 0, 1, 0));

        DefaultLayout layout = new DefaultLayout();
        layout.addButton(new TouchInputButton(0, 0, 100, 100), new CameraInputInterpreter(getActiveCamera()));
        InputEngineController.INSTANCE.setLayout(layout);
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));

    }
}
