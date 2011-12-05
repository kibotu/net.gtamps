package net.gtamps.android.game.content.scenes.layouts;

import android.view.MotionEvent;
import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.shared.Config;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.FloatData;

public class HudInterpreter extends InputInterpreter {

    private long startTime = 0;

	public HudInterpreter(ActionType actionType){
		super(actionType);
	}

    @Override
    public void interpretTouch(float x, float y, MotionEvent event) {

        // decrease input spamming
        if(System.currentTimeMillis()-startTime <= Config.HUD_INPUT_MESSAGE_TIMOUT) return;
        startTime = System.currentTimeMillis();

        if(actionType.equals(ActionType.PLAYER_MOVEMENT)){

            // accelerate
            if(y < 0.30f){
               eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, new FloatData((0.30f-y)*2f));
            }

            // decelerate
            if(y > 0.60f){
                eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, new FloatData((y-0.30f)*2f));
            }

            // left
            if(x < 0.30f){
                eventDispatcher.dispatch(SendableType.ACTION_LEFT, new FloatData((0.60f-x)*2f));
            }

            // right
            if(x > 0.60f){
                eventDispatcher.dispatch(SendableType.ACTION_RIGHT, new FloatData((x-0.60f)*2f));
            }
        }

        if(actionType.equals(ActionType.PLAYER_SHOOT)){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                eventDispatcher.dispatch(SendableType.ACTION_SHOOT, null);
            }
        }

        if(actionType.equals(ActionType.MUTE_SOUND)){

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
