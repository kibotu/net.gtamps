package net.gtamps.shared.serializer;

import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.Message;

@Deprecated
public class XmlSerializer implements ISerializer {

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
