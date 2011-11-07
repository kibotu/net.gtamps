package net.gtamps.android.core.input.layout;

import java.util.LinkedList;

import net.gtamps.android.core.input.event.InputEventDispatcher;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.touch.TouchInputWindow;

public abstract class AbstractInputLayout {
	TouchInputWindow touchWindow = new TouchInputWindow();
	InputEventDispatcher inputEventDispatcher;
	LinkedList<InputInterpreter> inputInterpreterList = new LinkedList<InputInterpreter>();
	
	public TouchInputWindow getTouchWindow() {
		return touchWindow;
	}

	public void setInputEventDispatcher(InputEventDispatcher inputEventDispatcher){
		this.inputEventDispatcher = inputEventDispatcher;
		for(InputInterpreter iip : inputInterpreterList){
			iip.setEventDispatcher(inputEventDispatcher);
		}
	}
	
}
