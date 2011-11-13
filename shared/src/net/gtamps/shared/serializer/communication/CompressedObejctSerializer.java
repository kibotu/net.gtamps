package net.gtamps.shared.serializer.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import net.gtamps.shared.Utils.Logger;

import org.jetbrains.annotations.NotNull;

public class CompressedObejctSerializer implements ISerializer{
	
    public final String TAG = this.getClass().getSimpleName();

    private InflaterInputStream inflaterInputStream;
    private DeflaterOutputStream deflaterOutputStream;
    private ByteArrayOutputStream byteOutputStream;
    private ByteArrayInputStream byteInputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    
	public byte[] serializeMessage(@NotNull Message message) {
        try {
            byteOutputStream = new ByteArrayOutputStream();
            deflaterOutputStream = new DeflaterOutputStream(byteOutputStream);
            objectOutputStream = new ObjectOutputStream(deflaterOutputStream);
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            Logger.printException(this, e);
        }
        return byteOutputStream.toByteArray();
    }

    @Override
    public Message deserializeMessage(@NotNull byte[] bytes) {
        Message message = null;
        try {
            byteInputStream = new ByteArrayInputStream(bytes);
            inflaterInputStream = new InflaterInputStream(byteInputStream);
            objectInputStream = new ObjectInputStream(inflaterInputStream);
            message = (Message) objectInputStream.readObject();
        } catch (IOException e) {
            Logger.printException(this, e);
        } catch (ClassNotFoundException e) {
            Logger.printException(this, e);
        }
        return message;
    }
}
