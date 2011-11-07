package net.gtamps.android.core.input;

import net.gtamps.android.core.input.touch.TouchInputButton;
import net.gtamps.android.core.input.touch.TouchInputWindow;



public class InputEngineController{
	private static InputEngineController instance;
	private static TouchInputWindow touchWindow;
	private static InputEventDispatcher inputEventDispatcher;
	
	private InputEngineController(){
		touchWindow = new TouchInputWindow();
		inputEventDispatcher = new InputEventDispatcher();
		
		InputInterpreter playerMovement = new InputInterpreter(inputEventDispatcher, ActionType.PLAYER_MOVEMENT);
		InputInterpreter muteSound = new InputInterpreter(inputEventDispatcher, ActionType.MUTE_SOUND);
		
		TouchInputButton movementButton = new TouchInputButton(0.2f, 0.2f, 0.2f, 0.2f);
		touchWindow.addButton(movementButton, playerMovement);
		
		TouchInputButton muteButton = new TouchInputButton(0.5f, 0.2f, 0.2f, 0.2f);
		touchWindow.addButton(muteButton, muteSound);
	}
	
	public static InputEngineController getInstance(){
		if(instance==null){
			instance = new InputEngineController();	
		}
		return instance;
	}
	
	public InputEventDispatcher getInputEventDispatcher() {
		getInstance();
		return inputEventDispatcher;
	}
	public TouchInputWindow getTouchInputWindow() {
		return touchWindow;
	}
}
