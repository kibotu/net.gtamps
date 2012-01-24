package net.gtamps.android.game.content.scenes.layouts;

import android.view.MotionEvent;
import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.SendableType;

public class HudInterpreter extends InputInterpreter {

    private long startTime = 0;

    public HudInterpreter(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void interpretTouch(float x, float y, MotionEvent event) {

        // decrease input spamming
        if (System.currentTimeMillis() - startTime <= Config.HUD_INPUT_MESSAGE_TIMOUT) {
        	return;
        }
        startTime = System.currentTimeMillis();

        if (actionType.equals(ActionType.PLAYER_MOVEMENT)) {

            // accelerate
            if (y < 0.30f) {
                eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, null);
            }
            // decelerate
            else if (y > 0.70f) {
                eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, null);
            }

            // left
            if (x < 0.30f) {
                eventDispatcher.dispatch(SendableType.ACTION_LEFT, null);
            }
            // right
            else if (x > 0.70f) {
                eventDispatcher.dispatch(SendableType.ACTION_RIGHT, null);
            }
        }

        if (actionType.equals(ActionType.PLAYER_SHOOT)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                eventDispatcher.dispatch(SendableType.ACTION_SHOOT, null);
            }
        }
        
        if (actionType.equals(ActionType.PLAYER_ENTER_CAR)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                eventDispatcher.dispatch(SendableType.ACTION_ENTEREXIT, null);
            }
        }

        if (actionType.equals(ActionType.MUTE_SOUND)) {

        }
    }

    /*@Override
    public void interpretTouch(float x, float y, MotionEvent event) {
        if(actionType.equals(ActionType.PLAYER_MOVEMENT)){
			if(y<0.5f){
				eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, new FloatData((0.5f-y)*2f));
			} else {
				eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, new FloatData((y-0.5f)*2f));
			}
			if(x<0.5f){
				eventDispatcher.dispatch(SendableType.ACTION_LEFT, new FloatData((0.5f-x)*2f));
			} else {
				eventDispatcher.dispatch(SendableType.ACTION_RIGHT, new FloatData((x-0.5f)*2f));
			}

		}
		if(actionType.equals(ActionType.PLAYER_SHOOT)){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				eventDispatcher.dispatch(SendableType.ACTION_SHOOT, null);
			}
		}
		if(actionType.equals(ActionType.MUTE_SOUND)){

		}
    } */


}
