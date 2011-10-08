package net.gtamps.android.game.client;

import android.text.Html;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class XmlSerializer implements ISerializer {

    private static final String TAG = XmlSerializer.class.getSimpleName();

    private final ByteArrayOutputStream byteOutputStream;
    private ObjectOutputStream objectOutputStream;
    private final ByteArrayInputStream byteInputStream;
    private ObjectInputStream objectInputStream;

    public XmlSerializer() {
        byteOutputStream = new ByteArrayOutputStream();
        objectOutputStream = null;
        byteInputStream = new ByteArrayInputStream();
        objectInputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectInputStream = new ObjectInputStream(byteInputStream);
        } catch (IOException e) {
            Utils.log(TAG, "" + e.getMessage());
        }
    }

    @Override
    public byte[] serializeMessage(@NotNull Message message) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            Utils.log(TAG, "" + e.getMessage());
        }
        return byteOutputStream.toByteArray();
    }

    @Override
    public Message deserializeMessage(@NotNull byte[] bytes) {

        return null;
    }
}
