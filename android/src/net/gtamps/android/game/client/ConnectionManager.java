package net.gtamps.android.game.client;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionManager implements IMessageManager {

    private static final String TAG = ConnectionManager.class.getSimpleName();

    private final ConcurrentLinkedQueue<Message> inbox;
    private final ConcurrentLinkedQueue<Message> outbox;
    private IStream stream;
    private RemoteInputDispatcher remoteInputDispatcher;
    private RemoteOutputDispatcher remoteOutputDispatcher;
    public static volatile String currentSessionId;
    public static volatile long currentRevId;

    private static ISerializer serializer = new ObjectSerializer();

    public ConnectionManager() {
        stream = new TcpStream();
        inbox = new ConcurrentLinkedQueue<Message>();
        outbox = new ConcurrentLinkedQueue<Message>();
        currentSessionId = "0";
        currentRevId = 0;
    }

    @Override
    public boolean isEmpty() {
        return inbox.isEmpty();
    }

    @Override
    public Message poll() {
        Message message = inbox.poll();
        currentSessionId = message.getSessionId();
        Logger.i(this, "inbox poll: " + message.toString());
        return message;
    }

    @Override
    public boolean add(@NotNull Message message) {
        message.setSessionId(currentSessionId);
        Logger.i(this, "outbox add: " + message.toString());
        return outbox.add(message);
    }

    public boolean connect() {
        return connect(Config.SERVER_HOST_ADDRESS, Config.SERVER_PORT);
    }

    public boolean connect(String host, int port) {
        if(isConnected()) return false;
        stream.connect(host, port);
        remoteInputDispatcher = new RemoteInputDispatcher(stream.getInputStream(), inbox);
        remoteOutputDispatcher = new RemoteOutputDispatcher(stream, outbox);
        return stream.isConnected();
    }

    public boolean disconnect() {
        if(!isConnected()) return false;
        remoteInputDispatcher.stop();
        remoteOutputDispatcher.stop();
        return stream.disconnect();
    }

    public boolean isConnected() {
        return stream.isConnected();
    }

    public void start() {
        if(!isConnected()) return;
        remoteInputDispatcher.start();
        remoteOutputDispatcher.start();
    }

    public void stop() {
        if(!isConnected()) return;
        remoteInputDispatcher.stop();
        remoteOutputDispatcher.stop();
    }

    public static byte [] serialize(@NotNull Message message) {
        message.setSessionId(currentSessionId);
        return serializer.serializeMessage(message);
    }

    public static Message deserialize(@NotNull byte[] message) {
        return serializer.deserializeMessage(message);
    }
}