package net.gtamps.android.game.client;

import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;

public class XmlSerializer implements ISerializer {

    public XmlSerializer() {
    }

    @Override
    public byte[] serializeMessage(Message m) {
        return new byte[0];
    }

    @Override
    public Message deserializeMessage(byte[] bytes) {
        return null;
    }
}