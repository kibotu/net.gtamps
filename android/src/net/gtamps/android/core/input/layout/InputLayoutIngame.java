package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.touch.TouchInputButton;
import net.gtamps.android.game.content.scenes.layouts.HudInterpreter;

public class InputLayoutIngame extends AbstractInputLayout {

    public InputLayoutIngame() {

        InputInterpreter playerMovement = new HudInterpreter(ActionType.PLAYER_MOVEMENT);
        TouchInputButton movementButton = new TouchInputButton(0.25f, 0f, 0.5f, 1f);
        addButton(movementButton, playerMovement);

        InputInterpreter playerShoot = new HudInterpreter(ActionType.PLAYER_SHOOT);
        TouchInputButton shootButton = new TouchInputButton(0f, 0f, 0.25f, 1f);
        addButton(shootButton, playerShoot);

        /*
          InputInterpreter muteSound = new InputInterpreter(ActionType.MUTE_SOUND);
          inputInterpreterList.add(muteSound);

          TouchInputButton muteButton = new TouchInputButton(0.5f, 0.2f, 0.2f, 0.2f);
          touchWindow.addButton(muteButton, muteSound);
          */
    }
}
