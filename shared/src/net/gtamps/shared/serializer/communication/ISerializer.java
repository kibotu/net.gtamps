package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.serializer.helper.SerializedMessage;

public interface ISerializer {
	
	SerializedMessage serializedMessage = new SerializedMessage();
	
	byte[] serializeNewMessage(NewMessage m);

	public NewMessage deserializeNewMessage(byte[] bytes);
	
	SerializedMessage serializeAndPackNewMessage(NewMessage m);

}
