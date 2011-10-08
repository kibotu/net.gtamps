package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ObjectSerializer implements ISerializer {

    private static final String TAG = ObjectSerializer.class.getSimpleName();

    private ByteArrayOutputStream byteOutputStream;
    private ByteArrayInputStream byteInputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ObjectSerializer() {
    }

    @Override
    public byte[] serializeMessage(@NotNull Message message) {
        try {
            byteOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            Utils.log(TAG, e.getMessage());
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
            Utils.log(TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Utils.log(TAG, e.getMessage());
        }
        return message;
    }
}
