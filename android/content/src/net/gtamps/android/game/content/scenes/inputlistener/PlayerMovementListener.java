package net.gtamps.android.game.content.scenes.inputlistener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.MessageBuilder;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class PlayerMovementListener implements InputEventListener {

	public PlayerMovementListener() {
		ConnectionManager.INSTANCE.add(NewMessageFactory.createLoginRequest("til", "secretpassword"));
		ConnectionManager.INSTANCE.add(NewMessageFactory.createGetUpdateRequest(0));
		ConnectionManager.INSTANCE.add(NewMessageFactory.createJoinRequest());
		
	}

	// preallocate
	NewMessage message;

	@Override
	public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
		Logger.d(this, "Received: " + sendableType.toString());
		Logger.d(this, "Current revision Id" +ConnectionManager.INSTANCE.currentRevId);
		if (sendableType.equals(SendableType.ACTION_ACCELERATE)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createAccelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_DECELERATE)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createDecelerateCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_LEFT)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createLeftCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_RIGHT)) {
			ConnectionManager.INSTANCE.add(NewMessageFactory.createRightCommand(1f));
		} else if (sendableType.equals(SendableType.ACTION_SHOOT)) {
			// ConnectionManager.INSTANCE.add(NewMessageFactory.createShootCommand();
		}
		message = NewMessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
		ConnectionManager.INSTANCE.add(message);

	}
}
