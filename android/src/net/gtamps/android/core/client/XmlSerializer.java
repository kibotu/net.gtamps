package net.gtamps.android.core.client;

import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;

@Deprecated
public class XmlSerializer implements ISerializer{

    public XmlSerializer() {
    }

    @Override
    public byte[] serializeMessage(Message message) {
        return new byte[0];
    }

    @Override
    public Message deserializeMessage(byte[] bytes) {
        return null;
    }
}
