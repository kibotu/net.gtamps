package net.gtamps.android.core.input.event;

import android.view.MotionEvent;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.FloatData;

public class InputInterpreter {
	
	private ActionType actionType;
	private InputEventDispatcher eventDispatcher;
	public InputInterpreter(ActionType actionType){
		this.actionType = actionType;
	}
	/**
	 * 
	 * @param x
	 * 		in range of 0f..1f inside the button
	 * @param y
	 * 		in range of 0f..1f inside the button
	 * 		where top is 0f and bottom is 1f
	 * @param event 
	 * 		to sense where it was ACTION_DOWN, ACTION_UP and so on.
	 */
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
	}

	public void setEventDispatcher(InputEventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
}
