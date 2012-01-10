package net.gtamps.android.game.content.scenes.inputlistener;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.MessageFactory;
import net.gtamps.shared.serializer.communication.Sendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class PlayerMovementListener implements InputEventListener {

    public PlayerMovementListener() {
    }

    //preallocate
    Message message;

    @Override
    public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
//		Logger.d(this, "Received: " + sendableType.toString());

        if (
                sendableType.equals(SendableType.ACTION_ACCELERATE) ||
                        sendableType.equals(SendableType.ACTION_DECELERATE) ||
                        sendableType.equals(SendableType.ACTION_LEFT) ||
                        sendableType.equals(SendableType.ACTION_RIGHT) ||
                        sendableType.equals(SendableType.ACTION_SHOOT)

                ) {
            message = MessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
            message.addSendable(new Sendable(sendableType, data));
            ConnectionManager.INSTANCE.add(message);
        }
    }
}
