package net.gtamps.android.graphics.test.input;

import android.view.MotionEvent;
import android.view.View;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.input.controller.event.InputInterpreter;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;

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
    private float lastZoomDistance;
    private Vector3 lastMidPoint = Vector3.createNew();

    @Override
    public void interpretTouch(float x, float y, View view, MotionEvent event) {

        final int action = event.getAction();
        final int actionIndex = event.getActionIndex();

        switch (action & MotionEvent.ACTION_MASK) {

            // Primary Pointer Down
            case MotionEvent.ACTION_DOWN:
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

                // drag or zoom
                if (event.getPointerCount() == 1) {

                    // move camera only if difference between frames is higher than touch screen precision
                    if (Math.abs(dx) > event.getXPrecision() || Math.abs(dy) > event.getYPrecision()) camera.move(dx / friction, dy / friction, 0);
                    lastX = px;
                    lastY = py;
                    
                } else {

                    float newDistance = hypot(event.getX(0), event.getX(0), event.getX(1), event.getY(1));
                    if (newDistance > 5f) {
                        camera.setZoomFactor(newDistance / lastZoomDistance);
                        updateFriction(view);
                    }
                }

                break;

            // Primary Pointer Down
            case MotionEvent.ACTION_CANCEL:
                break;

            // Primary Pointer Outside
            case MotionEvent.ACTION_OUTSIDE:
                break;

            // Secondary Pointer Down
            case MotionEvent.ACTION_POINTER_DOWN:

                // set origin for zoom
                lastZoomDistance = hypot(event.getX(0), event.getY(0), event.getX(1), event.getY(1));

                // set last mid point
                midPoint(lastMidPoint,event.getX(0), event.getY(0), event.getX(1), event.getY(1));

                break;

            // Secondary Pointer Up
            case MotionEvent.ACTION_POINTER_UP:

                // which pointer is up
//                final int pointerIndex = (action & 0x0000ff00) >> 0x00000008;
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);

                break;
        }
    }

    public static void midPoint(Vector3 point, float x0, float y0, float x1, float y1) {
        point.set((x0 + x1) / 2, (y0+y1) / 2,0);
    }

    private void updateFriction(View view) {
        float distanceToViewField = camera.getDistanceToViewField();
        float viewwidth = computeHorizontalFieldOfView(camera.getHorizontalFieldOfViewEffective(), distanceToViewField);
        friction = view.getWidth() / viewwidth / 2;
    }

    public static float hypot(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot((x1 - x2), (y1 - y2));
    }

     /*
     *
     *          	           B
     *                        /|
     *                      /  |
     *                    /    |
     *                  /      | a
     *                /        |
     *              /          |
     *           A/alpha-------| C
     *                   b
     *
     * a = tan(alpha) * b
     *
     *
     * @param zoom angel horizontal
     * @param distance to target
     * @return view horizontal
     */
    public float computeHorizontalFieldOfView(float zoomAngel, float distance){
        return (float) Math.tan(Math.toRadians(zoomAngel*0.5))*distance*2;
    }

}
