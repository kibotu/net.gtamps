package net.gtamps.android.input.controller;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import net.gtamps.android.input.controller.event.InputEventDispatcher;
import net.gtamps.android.input.view.AbstractInputLayout;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 16:12
 */
public enum InputEngineController implements View.OnTouchListener, View.OnKeyListener {

    INSTANCE;

    private InputEventDispatcher inputEventDispatcher;
    private AbstractInputLayout layout;

    private InputEngineController() {
        inputEventDispatcher = new InputEventDispatcher();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        // TODO handle key events
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return layout == null ? false : layout.getTouchPanel().onTouch(view, motionEvent);
    }

    public void setLayout(AbstractInputLayout layout) {
        this.layout = layout;
    }

    public InputEventDispatcher getInputEventDispatcher() {
        return inputEventDispatcher;
    }
}
