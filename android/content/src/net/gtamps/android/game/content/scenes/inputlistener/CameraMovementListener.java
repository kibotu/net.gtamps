package net.gtamps.android.game.content.scenes.inputlistener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.core.net.ConnectionManager;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.android.fakerenderer.FakeCamera;
import net.gtamps.android.simple3Drenderer.SimpleCamera;
import net.gtamps.android.simple3Drenderer.SimpleWorld;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.MessageBuilder;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class CameraMovementListener implements InputEventListener {

	IWorld world;
	private SimpleCamera camera; 
	public CameraMovementListener(IWorld world) {
		this.world = world;
	}

	public CameraMovementListener(SimpleWorld world2, SimpleCamera camera) {
		this.world = world2;
		this.camera = camera;
	}

	// preallocate
	NewMessage message;

	@Override
	public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
		if (sendableType.equals(SendableType.ACTION_ACCELERATE)) {
//			if(this.camera!=null){
//				camera.move(1,0,0);
//			}
			ConnectionManager.INSTANCE.add(NewMessageFactory.createAccelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_DECELERATE)) {
//			if(this.camera!=null){
//				camera.move(-1,0,0);
//			}
			ConnectionManager.INSTANCE.add(NewMessageFactory.createDecelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_LEFT)) {
//			if(this.camera!=null){
//				camera.move(0,-1,0);
//			}
			ConnectionManager.INSTANCE.add(NewMessageFactory.createLeftCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_RIGHT)) {
//			if(this.camera!=null){
//				camera.move(0,1,0);
//			}
			ConnectionManager.INSTANCE.add(NewMessageFactory.createRightCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_SHOOT)) {
//			if(this.camera!=null){
//				camera.move(0,0,-1);
//			}
			ConnectionManager.INSTANCE.add(NewMessageFactory.createShootCommand());
		} else if (sendableType.equals(SendableType.ACTION_ENTEREXIT)) {
//			if(this.camera!=null){
//				camera.move(0,0,1);
//			}
			ConnectionManager.INSTANCE.add(NewMessageFactory.createEnterExitCommand());
		}
		message = NewMessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
		ConnectionManager.INSTANCE.add(message);

	}
}
