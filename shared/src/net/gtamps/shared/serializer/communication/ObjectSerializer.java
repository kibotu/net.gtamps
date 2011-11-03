package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.serializer.communication.data.UpdateData;
import org.jetbrains.annotations.NotNull;

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
    public byte[] serializeMessage(@NotNull Message message) {

        //DEBUG
        for (Sendable sendable : message.sendables) {
            if (sendable.type.equals(SendableType.GETUPDATE_OK)) {
                UpdateData data = (UpdateData) sendable.data;
                for (GameObject go : data.gameObjects) {
                    Class<? extends GameObject> c = go.getClass();
                    if (!Entity.class.isAssignableFrom(c) && !GameEvent.class.isAssignableFrom(c)) {
//   					if (Entity.class.equals(c) ) {
                        throw new IllegalStateException(go.toString());
                    }
                    for (IProperty p : go.getAllProperties()) {
                        System.out.println(p.toString());
                    }
                }
            }
        }

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
