package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.InputEventDispatcher;
import net.gtamps.android.core.input.touch.TouchInputWindow;

public class AbstractInputLayout {
	TouchInputWindow touchWindow = new TouchInputWindow();
	InputEventDispatcher inputEventDispatcher;
	
	public TouchInputWindow getTouchWindow() {
		return touchWindow;
	}

	public void setInputEventDispatcher(InputEventDispatcher inputEventDispatcher) {
		this.inputEventDispatcher = inputEventDispatcher;
	}
	
}
