package net.gtamps.android.gtandroid.core.input.layout;

import net.gtamps.android.gtandroid.core.input.event.InputEventDispatcher;
import net.gtamps.android.gtandroid.core.input.event.InputInterpreter;
import net.gtamps.android.gtandroid.core.input.touch.TouchInputButton;
import net.gtamps.android.gtandroid.core.input.touch.TouchInputWindow;

import java.util.LinkedList;

public abstract class AbstractInputLayout {
    private TouchInputWindow touchWindow = new TouchInputWindow();
    private InputEventDispatcher inputEventDispatcher;
    private LinkedList<InputInterpreter> inputInterpreters = new LinkedList<InputInterpreter>();

    public TouchInputWindow getTouchWindow() {
        return touchWindow;
    }

    public void setInputEventDispatcher(InputEventDispatcher inputEventDispatcher) {
        this.inputEventDispatcher = inputEventDispatcher;
        for (InputInterpreter iip : inputInterpreters) {
            iip.setEventDispatcher(inputEventDispatcher);
        }
    }

    public void addButton(TouchInputButton touchInputButton, InputInterpreter inputInterpreter) {
        inputInterpreters.add(inputInterpreter);
        this.touchWindow.addButton(touchInputButton, inputInterpreter);
    }

}
