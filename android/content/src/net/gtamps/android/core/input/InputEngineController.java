package net.gtamps.android.core.input;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import net.gtamps.android.core.input.event.InputEventDispatcher;
import net.gtamps.android.core.input.layout.AbstractInputLayout;
import net.gtamps.android.core.input.layout.DummyLayout;


public class InputEngineController implements OnTouchListener, OnKeyListener {
    private static InputEngineController instance;
    private static AbstractInputLayout layout;
    private static InputEventDispatcher inputEventDispatcher;


    private InputEngineController() {

    }

    public static InputEngineController getInstance() {
        if (instance == null) {
            instance = new InputEngineController();
        }
        return instance;
    }

    public void setLayout(AbstractInputLayout layout) {
        inputEventDispatcher = new InputEventDispatcher();
        layout.setInputEventDispatcher(inputEventDispatcher);
        InputEngineController.layout = layout;
    }

    public InputEventDispatcher getInputEventDispatcher() {
        return inputEventDispatcher;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return layout.getTouchWindow().onTouch(v, event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
        //TODO
        //return layout.getKeyBoardHandler().onKey()
    }

}