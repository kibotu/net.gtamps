package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.Utils.Logger;
import java.io.*;

public class ObjectSerializer implements ISerializer {

	public final String TAG = this.getClass().getSimpleName();

	private ByteArrayOutputStream byteOutputStream;
	private ByteArrayInputStream byteInputStream;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	public ObjectSerializer() {
	}

	@Override
	public byte[] serializeNewMessage(final NewMessage message) {
		try {
			byteOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(message);
		} catch (final IOException e) {
			Logger.printException(this, e);
		}
		return byteOutputStream.toByteArray();
	}

	@Override
	public NewMessage deserializeNewMessage(final byte[] bytes) {
		NewMessage message = null;
		try {
			byteInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteInputStream);
			message = (NewMessage) objectInputStream.readObject();
		} catch (final IOException e) {
			Logger.printException(this, e);
		} catch (final ClassNotFoundException e) {
			Logger.printException(this, e);
		}
		return message;
	}

}
