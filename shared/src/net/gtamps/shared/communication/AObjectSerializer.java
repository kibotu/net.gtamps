package net.gtamps.shared.communication;

import android.text.Html;
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
            printException(TAG, e);
            log(TAG, e.getClass().getSimpleName() + " " +e.getMessage());
        }
        return byteOutputStream.toByteArray();
    }

    public void printException(String tag, Exception e) {
        StringBuilder error = new StringBuilder();
        for(StackTraceElement stackTraceElement: e.getStackTrace()) {
            error.append(stackTraceElement + "\n");
        }
        log(tag, error.toString());
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
            printException(TAG, e);
            log(TAG, e.getClass().getSimpleName() + " " +e.getMessage());
        } catch (ClassNotFoundException e) {
            printException(TAG, e);
            log(TAG, e.getClass().getSimpleName() + " " +e.getMessage());
        }
        return message;
    }
}
