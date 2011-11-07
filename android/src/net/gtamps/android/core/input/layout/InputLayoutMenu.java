package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.touch.TouchInputButton;

public class InputLayoutMenu extends AbstractInputLayout{
	public InputLayoutMenu(){
		InputInterpreter playerMovement = new InputInterpreter(inputEventDispatcher, ActionType.PLAYER_MOVEMENT);
		TouchInputButton movementButton = new TouchInputButton(0.2f, 0.2f, 0.2f, 0.2f);
		touchWindow.addButton(movementButton, playerMovement);
		
		InputInterpreter muteSound = new InputInterpreter(inputEventDispatcher, ActionType.MUTE_SOUND);
		TouchInputButton muteButton = new TouchInputButton(0.5f, 0.2f, 0.2f, 0.2f);
		touchWindow.addButton(muteButton, muteSound);
	}
}
