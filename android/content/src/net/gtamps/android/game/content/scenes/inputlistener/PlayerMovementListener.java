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
    }

    //preallocate
    NewMessage message;

    @Override
    public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
		Logger.d(this, "Received: " + sendableType.toString());

        if (
                sendableType.equals(SendableType.ACTION_ACCELERATE) ||
                        sendableType.equals(SendableType.ACTION_DECELERATE) ||
                        sendableType.equals(SendableType.ACTION_LEFT) ||
                        sendableType.equals(SendableType.ACTION_RIGHT) ||
                        sendableType.equals(SendableType.ACTION_SHOOT)

                ) {
            message = NewMessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
            //FIXME
//            message.addSendable(new NewSendable(sendableType, data));
            ConnectionManager.INSTANCE.add(message);
        }
    }
}
