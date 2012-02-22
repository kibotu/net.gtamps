package net.gtamps.android.graphics.test.input.layout;

import android.view.MotionEvent;
import android.view.View;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.input.controller.event.InputInterpreter;

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

    private float lastX;
    private float lastY;
    private float friction = 100;   //  pointer movement (in pixel) * value = actual velocity

    @Override
    public void interpretTouch(float x, float y, View view, MotionEvent event) {

        final int action = event.getAction();
        final int actionIndex = event.getActionIndex();


        switch (action & MotionEvent.ACTION_MASK) {

            // Primary Pointer Down
            case MotionEvent.ACTION_DOWN:
                lastX = 0;
                lastY = 0;
                break;

            // Primary Pointer Up
            case MotionEvent.ACTION_UP:
                break;

            // Primary Pointer Move
            case MotionEvent.ACTION_MOVE:

                final float px = event.getX() - view.getWidth() * 0.5f;
                final float py = event.getY() - view.getHeight() * 0.5f;

                final float dx = lastX - px;
                final float dy = py - lastY;

                // move camera only if difference between frames is higher than touch screen precision
                if (Math.abs(dx) > event.getXPrecision() || Math.abs(dy) > event.getYPrecision()) {
                    camera.move(dx / friction, dy / friction, 0);
                }

                lastX = px;
                lastY = py;
                break;

            // Primary Pointer Down
            case MotionEvent.ACTION_CANCEL:
                break;

            // Primary Pointer Outside
            case MotionEvent.ACTION_OUTSIDE:
                break;

            // Secondary Pointer Down
            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            // Secondary Pointer Up
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
    }
}
