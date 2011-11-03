package net.gtamps.shared.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.communication.data.UpdateData;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;

import org.jetbrains.annotations.NotNull;

public class ObjectSerializer implements ISerializer{

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
    				if (!Entity.class.isAssignableFrom(c) || !GameEvent.class.isAssignableFrom(c)) {
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
