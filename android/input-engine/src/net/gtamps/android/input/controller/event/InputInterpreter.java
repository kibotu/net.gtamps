package net.gtamps.android.input.controller.event;

import android.view.MotionEvent;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 16:30
 */
public interface InputInterpreter {

    /**
     * @param x in range of 0f..1f inside the button
     * @param y in range of 0f..1f inside the button
     *          <p/>
     *          <p>(0,0)----(1,0)</p>
     *          <p>&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|</p>
     *          <p>&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|</p>
     *          <p>(0,1)----(1,1)</p>
     */
    public abstract void interpretTouch(float x, float y, MotionEvent event);
}

