package net.gtamps.android.game.content.scenes.inputlistener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.core.net.ConnectionManager;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.MessageBuilder;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;
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
			ConnectionManager.INSTANCE.add(NewMessageFactory.createAccelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_DECELERATE)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createDecelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_LEFT)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createLeftCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_RIGHT)) {
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
