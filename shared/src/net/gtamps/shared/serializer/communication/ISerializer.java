package net.gtamps.shared.serializer.communication;

public interface ISerializer {
	byte[] serializeNewMessage(NewMessage m);

	public NewMessage deserializeNewMessage(byte[] bytes);
}
