package net.gtamps.android.core.input;

import net.gtamps.shared.serializer.communication.SendableType;

public class InputInterpreter {
	
	private ActionType actionType;
	private InputEventDispatcher eventDispatcher;
	public InputInterpreter(InputEventDispatcher evdi, ActionType actionType){
		this.eventDispatcher = evdi;
		this.actionType = actionType;
	}
	/**
	 * 
	 * @param x
	 * 		in range of 0f..1f inside the button
	 * @param y
	 * 		in range of 0f..1f inside the button
	 * 
	 */
	public void interpretTouch(float x, float y) {
		//TODO
		if(actionType.equals(ActionType.PLAYER_MOVEMENT)){
			if(y<0.5f){
				eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, (0.5f-y)*2f);
			} else {
				eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, (y-0.5f)*2f);	
			}
			if(x<0.5f){
				eventDispatcher.dispatch(SendableType.ACTION_LEFT, (0.5f-x)*2f);		
			} else {
				eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, (x-0.5f)*2f);
			}
			
		}
		if(actionType.equals(ActionType.MUTE_SOUND)){
			
		}
	}

}
