package net.gtamps.android.game.content.scenes.layouts;


import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.layout.AbstractInputLayout;
import net.gtamps.android.core.input.touch.TouchInputButton;

public class HudLayout extends AbstractInputLayout {

    public HudLayout() {

        // shoot button
        InputInterpreter playerShoot = new HudInterpreter(ActionType.PLAYER_SHOOT);
        inputInterpreterList.add(playerShoot);
        TouchInputButton shootButton = new TouchInputButton(0f, 0f, 0.25f, 1f);
        touchWindow.addButton(shootButton, playerShoot);

        // movement button
        InputInterpreter playerMovement = new HudInterpreter(ActionType.PLAYER_MOVEMENT);
        inputInterpreterList.add(playerMovement);
        TouchInputButton movementButton = new TouchInputButton(0.25f, 0.2f, 0.6f, 0.6f);
        touchWindow.addButton(movementButton, playerMovement);
    }
}
