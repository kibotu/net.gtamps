package net.gtamps.android.core.input;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import net.gtamps.android.core.input.event.InputEventDispatcher;
import net.gtamps.android.core.input.layout.AbstractInputLayout;
import net.gtamps.android.core.input.layout.DummyLayout;
import net.gtamps.android.core.input.touch.TouchInputButton;
import net.gtamps.android.core.input.touch.TouchInputWindow;



public class InputEngineController implements OnTouchListener{
	private static InputEngineController instance;
	private static AbstractInputLayout layout;
	private static InputEventDispatcher inputEventDispatcher;
	
	
	private InputEngineController(){
		
	}
	
	public static InputEngineController getInstance(){
		if(instance==null){
			instance = new InputEngineController();	
			setLayout(new DummyLayout());
		}
		return instance;
	}
	
	public static void setLayout(AbstractInputLayout layout) {
		InputEngineController.layout = layout;
		inputEventDispatcher = new InputEventDispatcher();
		layout.setInputEventDispatcher(inputEventDispatcher);
	}
	
	public static InputEventDispatcher getInputEventDispatcher() {
		return inputEventDispatcher;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return layout.getTouchWindow().onTouch(v, event);
	}

}