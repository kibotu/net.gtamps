package net.gtamps.android.game.content.scenes.inputlistener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.fakerenderer.FakeCamera;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.MessageBuilder;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class CameraMovementListener implements InputEventListener {

	private FakeCamera camera;
	public CameraMovementListener(FakeCamera camera) {
		this.camera = camera;
	}

	// preallocate
	NewMessage message;

	@Override
	public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
		if (sendableType.equals(SendableType.ACTION_ACCELERATE)) {
//			camera.move(0,10);
			ConnectionManager.INSTANCE.add(NewMessageFactory.createAccelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_DECELERATE)) {
//			camera.move(0,-10);
			ConnectionManager.INSTANCE.add(NewMessageFactory.createDecelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_LEFT)) {
//			camera.move(10,0);
			ConnectionManager.INSTANCE.add(NewMessageFactory.createLeftCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_RIGHT)) {
//			camera.move(-10,0);
			ConnectionManager.INSTANCE.add(NewMessageFactory.createRightCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_SHOOT)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createShootCommand());
		} else if (sendableType.equals(SendableType.ACTION_ENTEREXIT)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createEnterExitCommand());
		}
		message = NewMessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
		ConnectionManager.INSTANCE.add(message);

	}
}
