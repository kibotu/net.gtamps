package net.gtamps.shared.serializer.communication;

public interface ISerializer {
    public byte[] serializeMessage(Message m);

    public Message deserializeMessage(byte[] bytes);
}
