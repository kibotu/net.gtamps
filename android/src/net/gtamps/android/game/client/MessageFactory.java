package net.gtamps.android.game.client;

import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.Message;

final public class MessageFactory {

    private MessageFactory() {
    }

    public static Message createCommand(Command command) {
        Message message = new Message();
        message.addSendable(command);
        return message;
    }

    public static Message create(byte[] response) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
