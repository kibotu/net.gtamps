package net.gtamps.android.core.input.layout;

import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.input.interpreter.HudInterpreter;
import net.gtamps.android.core.input.touch.TouchInputButton;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.android.renderer.graph.scene.primitives.Camera;

public class InputLayoutIngame extends AbstractInputLayout {

    public InputLayoutIngame(IWorld world) {

        InputInterpreter playerMovement = new HudInterpreter(ActionType.PLAYER_MOVEMENT, world);
        TouchInputButton movementButton = new TouchInputButton(0.25f, 0f, 0.5f, 1f);
        addButton(movementButton, playerMovement);

        InputInterpreter playerShoot = new HudInterpreter(ActionType.PLAYER_SHOOT, world);
        TouchInputButton shootButton = new TouchInputButton(0f, 0f, 0.25f, 1f);
        addButton(shootButton, playerShoot);

        InputInterpreter playerEnterExit = new HudInterpreter(ActionType.PLAYER_ENTER_CAR, world);
        TouchInputButton enterExitButton = new TouchInputButton(0.76f, 0f, 0.24f, 1f);
        addButton(enterExitButton, playerEnterExit);

    }
}
