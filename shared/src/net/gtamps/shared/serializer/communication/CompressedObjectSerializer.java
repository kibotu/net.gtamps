package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.helper.SerializedMessage;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;


public class CompressedObjectSerializer implements ISerializer {

	public final String TAG = this.getClass().getSimpleName();

	private ByteArrayOutputStream byteOutputStream;
	private ObjectOutputStream objectOutputStream;
	private DeflaterOutputStream deflaterOutputStream;


	private ByteArrayInputStream byteInputStream;
	private ObjectInputStream objectInputStream;
	private InflaterInputStream inflaterInputStream;
	private final int bufferSize;

	public CompressedObjectSerializer(final int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public CompressedObjectSerializer() {
		this(1024);
	}
	
	public CompressedObjectSerializer clone() {
		return new CompressedObjectSerializer(bufferSize);
	}

	@Override
	public byte[] serializeNewMessage(final NewMessage message) {
		try {
			byteOutputStream = new ByteArrayOutputStream();
			deflaterOutputStream = new GZIPOutputStream(byteOutputStream, bufferSize);
			objectOutputStream = new ObjectOutputStream(deflaterOutputStream);
			objectOutputStream.writeObject(message);
			deflaterOutputStream.finish();
		} catch (final IOException e) {
			Logger.printException(this, e);
		}

		return byteOutputStream.toByteArray();    }

	@Override
	public NewMessage deserializeNewMessage(final byte[] bytes) {
		NewMessage message = null;
		try {
			byteInputStream = new ByteArrayInputStream(bytes);
			inflaterInputStream = new GZIPInputStream(byteInputStream, bufferSize);
			objectInputStream = new ObjectInputStream(inflaterInputStream);
			message = (NewMessage) objectInputStream.readObject();
		} catch (final IOException e) {
			Logger.printException(this, e);
		} catch (final ClassNotFoundException e) {
			Logger.printException(this, e);
		}
		return message;
	}

	@Override
	public SerializedMessage serializeAndPackNewMessage(NewMessage m) {
		serializedMessage.message = serializeNewMessage(m);
		serializedMessage.length = serializedMessage.message.length;
		return serializedMessage;
	}

}
