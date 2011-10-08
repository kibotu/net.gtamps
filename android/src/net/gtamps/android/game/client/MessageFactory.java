package net.gtamps.android.game.client;

import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.Message;

final public class MessageFactory {

    private MessageFactory() {
    }

    public static Message createCommand(Command.Type type, int percent) {
        Message message = new Message();
        message.addSendable(new Command(type,percent));
        return message;
    }

    public static Message create(byte[] response) {
        return null;
    }
}
