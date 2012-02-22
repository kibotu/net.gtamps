package net.gtamps.android.graphics.test.input.layout;

import android.view.MotionEvent;
import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.input.controller.event.InputInterpreter;
import net.gtamps.shared.Utils.Logger;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 19:57
 */
public class CameraInputInterpreter implements InputInterpreter {

    private Camera camera;

    public CameraInputInterpreter(Camera activeCamera) {
        this.camera = activeCamera;
    }

    @Override
    public void interpretTouch(float x, float y, MotionEvent event) {
        Logger.I(this, "x=" + x + "|y=" + y);
    }
}
