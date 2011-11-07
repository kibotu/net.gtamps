package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputEventDispatcher;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.listener.CameraListener;
import net.gtamps.android.core.input.touch.TouchInputButton;

public class InputLayoutIngame extends AbstractInputLayout{
	public InputLayoutIngame(){
		InputInterpreter playerMovement = new InputInterpreter(ActionType.PLAYER_MOVEMENT);
		inputInterpreterList.add(playerMovement);
		
		TouchInputButton movementButton = new TouchInputButton(0f, 0f, 1f, 1f);
		touchWindow.addButton(movementButton, playerMovement);
		
		/*
		InputInterpreter muteSound = new InputInterpreter(ActionType.MUTE_SOUND);
		inputInterpreterList.add(muteSound);
		
		TouchInputButton muteButton = new TouchInputButton(0.5f, 0.2f, 0.2f, 0.2f);
		touchWindow.addButton(muteButton, muteSound);
		*/			
	}
}
