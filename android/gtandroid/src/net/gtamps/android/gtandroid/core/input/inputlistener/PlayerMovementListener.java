package net.gtamps.android.gtandroid.core.input.inputlistener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.core.net.AbstractConnectionManager;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class PlayerMovementListener implements InputEventListener {

	public PlayerMovementListener() {
	}

	// preallocate
	NewMessage message;

	@Override
	public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
		if (sendableType.equals(SendableType.ACTION_ACCELERATE)) {
			AbstractConnectionManager.getInstance().add(NewMessageFactory.createAccelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_DECELERATE)) {
			AbstractConnectionManager.getInstance().add(NewMessageFactory.createDecelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_LEFT)) {
			AbstractConnectionManager.getInstance().add(NewMessageFactory.createLeftCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_RIGHT)) {
			AbstractConnectionManager.getInstance().add(NewMessageFactory.createRightCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_SHOOT)) {
			AbstractConnectionManager.getInstance().add(NewMessageFactory.createShootCommand());
		} else if (sendableType.equals(SendableType.ACTION_ENTEREXIT)) {
			AbstractConnectionManager.getInstance().add(NewMessageFactory.createEnterExitCommand());
		}
		message = NewMessageFactory.createGetUpdateRequest(AbstractConnectionManager.getInstance().currentRevId);
		AbstractConnectionManager.getInstance().add(message);

	}
}
