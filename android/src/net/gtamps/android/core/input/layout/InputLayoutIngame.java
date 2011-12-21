package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.touch.TouchInputButton;

public class InputLayoutIngame extends AbstractInputLayout {

    public InputLayoutIngame() {

        InputInterpreter playerMovement = new HudInterpreter(ActionType.PLAYER_MOVEMENT);
        TouchInputButton movementButton = new TouchInputButton(0.25f, 0f, 0.5f, 1f);
        addButton(movementButton, playerMovement);

        InputInterpreter playerShoot = new HudInterpreter(ActionType.PLAYER_SHOOT);
        TouchInputButton shootButton = new TouchInputButton(0f, 0f, 0.25f, 1f);
        addButton(shootButton, playerShoot);

        InputInterpreter playerEnterExit = new HudInterpreter(ActionType.PLAYER_ENTER_CAR);
        TouchInputButton enterExitButton = new TouchInputButton(0.76f, 0f, 0.24f, 1f);
        addButton(enterExitButton, playerEnterExit);
        
	}
}
