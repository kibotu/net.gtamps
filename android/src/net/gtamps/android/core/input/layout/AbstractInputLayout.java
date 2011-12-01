package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.InputEventDispatcher;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.touch.TouchInputWindow;

import java.util.LinkedList;

public abstract class AbstractInputLayout {
	protected TouchInputWindow touchWindow = new TouchInputWindow();
	protected InputEventDispatcher inputEventDispatcher;
	protected LinkedList<InputInterpreter> inputInterpreterList = new LinkedList<InputInterpreter>();
	
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
