package net.gtamps.shared.serializer.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import net.gtamps.shared.Utils.Logger;

import org.jetbrains.annotations.NotNull;

public class CompressedObjectSerializer implements ISerializer {

    public final String TAG = this.getClass().getSimpleName();

    private ByteArrayOutputStream byteOutputStream;
    private ObjectOutputStream objectOutputStream;
    private DeflaterOutputStream deflaterOutputStream;
    
    
    private ByteArrayInputStream byteInputStream;
    private ObjectInputStream objectInputStream;
    private InflaterInputStream inflaterInputStream;
    private int bufferSize;
    
    public CompressedObjectSerializer(int bufferSize) {
    	this.bufferSize = bufferSize;
    }
    public CompressedObjectSerializer() {
    	this(1024);
    }	

    @Override
    public byte[] serializeMessage(@NotNull final Message message) {
        try {
            byteOutputStream = new ByteArrayOutputStream();
            deflaterOutputStream = new GZIPOutputStream(byteOutputStream,bufferSize);
            objectOutputStream = new ObjectOutputStream(deflaterOutputStream);
            objectOutputStream.writeObject(message);
            deflaterOutputStream.finish();
        } catch (final IOException e) {
            Logger.printException(this, e);
        }

        return byteOutputStream.toByteArray();
    }

    @Override
    public Message deserializeMessage(@NotNull final byte[] bytes) {
        Message message = null;
        try {
            byteInputStream = new ByteArrayInputStream(bytes);
            inflaterInputStream = new GZIPInputStream(byteInputStream,bufferSize);
            objectInputStream = new ObjectInputStream(inflaterInputStream);
            message = (Message) objectInputStream.readObject();
        } catch (final IOException e) {
            Logger.printException(this, e);
        } catch (final ClassNotFoundException e) {
            Logger.printException(this, e);
        }
        return message;
    }

}
