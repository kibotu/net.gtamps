package net.gtamps.shared.communication;

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
            log(TAG, e.getMessage());
        }
        return byteOutputStream.toByteArray();
    }

    protected abstract void log(String tag, String message);

    @Override
    public Message deserializeMessage(@NotNull byte[] bytes) {
        Message message = null;
        try {
            byteInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteInputStream);
            message = (Message) objectInputStream.readObject();
        } catch (IOException e) {
            log(TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            log(TAG, e.getMessage());
        }
        return message;
    }
}
