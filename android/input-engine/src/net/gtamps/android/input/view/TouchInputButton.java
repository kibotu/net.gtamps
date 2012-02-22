package net.gtamps.android.input.view;

import android.view.MotionEvent;
import android.view.View;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 16:28
 */
public class TouchInputButton {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    /**
     * Values are supposed to be normalized!
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public TouchInputButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isHit(float px, float py) {
        return px >= x && py >= y && px <= x + width && py <= y + height;
    }

    public void setPosition(int x, int y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}

