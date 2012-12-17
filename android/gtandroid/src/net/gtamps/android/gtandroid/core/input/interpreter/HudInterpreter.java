package net.gtamps.android.gtandroid.core.input.interpreter;

import android.view.MotionEvent;
import net.gtamps.android.core.input.event.ActionType;
import net.gtamps.android.core.input.event.InputInterpreter;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.shared.Config;
import net.gtamps.shared.serializer.communication.SendableType;

public class HudInterpreter extends InputInterpreter {

	private static final double JUST_GO_THERE_ANGLE = 20.0;
	private static final double JUST_GO_THERE_ANGLE_SINE = Math.sin(JUST_GO_THERE_ANGLE / 180.0 * Math.PI);
	private long startTime = 0;
	private double rotation;

	public HudInterpreter(ActionType actionType, IWorld world) {
		super(actionType, world);
	}

	double currentRotation = 0;
	double wantedRotation = 0;
	double directionTrigger = 0;

	@Override
	public void interpretTouch(float x, float y, MotionEvent event) {

		// decrease input spamming
		if (System.currentTimeMillis() - startTime <= Config.HUD_INPUT_MESSAGE_TIMOUT) {
			return;
		}
		startTime = System.currentTimeMillis();

		if (actionType.equals(ActionType.PLAYER_MOVEMENT)) {

			if (world.getActiveView() != null) {
				if (world.getActiveView().entity.getName().equals("CAR")) {
					if (x < 0.6 && x > 0.4) {
						if (y < 0.5f) {
							eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, null);
						} else {
							eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, null);
						}
					} else {
						if (x < 0.5f) {
							eventDispatcher.dispatch(SendableType.ACTION_LEFT, null);
						} else {
							eventDispatcher.dispatch(SendableType.ACTION_RIGHT, null);
						}
					}
				} else {
					// convert to radians
					currentRotation = world.getActiveView().entity.rota.value() / 180.0 * Math.PI;
					wantedRotation = Math.atan2((y - 0.5), (x - 0.5));

					directionTrigger = Math.sin(wantedRotation - currentRotation);
					if (Math.abs(directionTrigger) < JUST_GO_THERE_ANGLE_SINE) {
						eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, null);
					} else if (directionTrigger > 0) {
						eventDispatcher.dispatch(SendableType.ACTION_RIGHT, null);
					} else if (directionTrigger < 0) {
						eventDispatcher.dispatch(SendableType.ACTION_LEFT, null);

					}
				}
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

		/*
		 * if (actionType.equals(ActionType.CAMERA_MOVE)) {
		 * 
		 * // accelerate if (y < 0.50f) {
		 * camera.move((float)Math.cos(this.rotation),
		 * -(float)Math.sin(this.rotation), 0, false); // Logger.d(this,
		 * this.camera.toString()); } // decelerate else if (y > 0.50f) {
		 * camera.move((float)Math.sin(this.rotation),
		 * (float)Math.cos(this.rotation), 0, false); // Logger.d(this,
		 * this.camera.toString()); }
		 * 
		 * // left if (x < 0.50f) { this.rotation += Math.PI/120; Logger.d(this,
		 * this.rotation); } // right else if (x > 0.50f) { this.rotation -=
		 * Math.PI/120; Logger.d(this, this.rotation); } }
		 */
	}

	/*
	 * @Override public void interpretTouch(float x, float y, MotionEvent event)
	 * { if(actionType.equals(ActionType.PLAYER_MOVEMENT)){ if(y<0.5f){
	 * eventDispatcher.dispatch(SendableType.ACTION_ACCELERATE, new
	 * FloatData((0.5f-y)*2f)); } else {
	 * eventDispatcher.dispatch(SendableType.ACTION_DECELERATE, new
	 * FloatData((y-0.5f)*2f)); } if(x<0.5f){
	 * eventDispatcher.dispatch(SendableType.ACTION_LEFT, new
	 * FloatData((0.5f-x)*2f)); } else {
	 * eventDispatcher.dispatch(SendableType.ACTION_RIGHT, new
	 * FloatData((x-0.5f)*2f)); }
	 * 
	 * } if(actionType.equals(ActionType.PLAYER_SHOOT)){ if(event.getAction() ==
	 * MotionEvent.ACTION_DOWN){
	 * eventDispatcher.dispatch(SendableType.ACTION_SHOOT, null); } }
	 * if(actionType.equals(ActionType.MUTE_SOUND)){
	 * 
	 * } }
	 */

}
