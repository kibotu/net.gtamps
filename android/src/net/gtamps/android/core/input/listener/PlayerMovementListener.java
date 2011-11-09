package net.gtamps.android.core.input.listener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.MessageFactory;
import net.gtamps.shared.serializer.communication.Sendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.FloatData;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class PlayerMovementListener implements InputEventListener {

	public PlayerMovementListener() {
	}

	@Override
	public void onSendableRetrieve(SendableType sendableType, ISendableData data) {

		Message message = MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId);

		Logger.d(this, "Received: " + sendableType.toString());

		if (
			sendableType.equals(SendableType.ACTION_ACCELERATE) ||
			sendableType.equals(SendableType.ACTION_DECELERATE) ||
			sendableType.equals(SendableType.ACTION_LEFT) ||
			sendableType.equals(SendableType.ACTION_RIGHT)
		) {
			message.addSendable(new Sendable(sendableType, data));
		}
	}
}
