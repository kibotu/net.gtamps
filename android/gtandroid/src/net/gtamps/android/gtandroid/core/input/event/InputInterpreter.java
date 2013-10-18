package net.gtamps.android.gtandroid.core.input.event;

import android.view.MotionEvent;
import net.gtamps.android.gtandroid.core.net.IWorld;

public abstract class InputInterpreter {

    protected ActionType actionType;
    protected InputEventDispatcher eventDispatcher;
    protected IWorld world;

    public InputInterpreter(ActionType actionType, IWorld world) {
        this.actionType = actionType;
        this.world = world;
    }

    /**
     * @param x     in range of 0f..1f inside the button
     * @param y     in range of 0f..1f inside the button
     *              where top is 0f and bottom is 1f
     * @param event to sense whether it was ACTION_DOWN, ACTION_UP and so on.
     */
    public abstract void interpretTouch(float x, float y, MotionEvent event);

    public void setEventDispatcher(InputEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }
}
