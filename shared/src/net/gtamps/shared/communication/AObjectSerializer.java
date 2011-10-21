package net.gtamps.shared.communication;

import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public abstract class AObjectSerializer implements ISerializer{

    public final String TAG = this.getClass().getSimpleName();

    private ByteArrayOutputStream byteOutputStream;
    private ByteArrayInputStream byteInputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public AObjectSerializer() {
    }

    @Override
    public byte[] serializeMessage(@NotNull Message message) {
        try {
            byteOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteOutputStream);
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
            objectInputStream = new ObjectInputStream(byteInputStream);
            message = (Message) objectInputStream.readObject();
        } catch (IOException e) {
             Logger.printException(this, e);
        } catch (ClassNotFoundException e) {
            Logger.printException(this, e);
        }
        return message;
    }
}
