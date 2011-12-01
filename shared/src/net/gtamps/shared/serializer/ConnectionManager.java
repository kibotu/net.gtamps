package net.gtamps.shared.serializer;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.ObjectSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentLinkedQueue;

public enum ConnectionManager implements IMessageManager {

    INSTANCE;

    private final ConcurrentLinkedQueue<Message> inbox;
    private final ConcurrentLinkedQueue<Message> outbox;
    private IStream stream;
    private RemoteInputDispatcher remoteInputDispatcher;
    private RemoteOutputDispatcher remoteOutputDispatcher;
    public volatile String currentSessionId;
    public volatile long currentRevId;
    private int currentSocketTimeOut = Config.MAX_SOCKET_TIMEOUT;

    private static ISerializer serializer = new ObjectSerializer();

    private ConnectionManager() {
        stream = new TcpStream();
        inbox = new ConcurrentLinkedQueue<Message>();
        outbox = new ConcurrentLinkedQueue<Message>();
        this.currentSessionId = "0";
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
        return connect(Config.SERVER_DEFAULT_HOST_ADDRESS, Config.SERVER_DEFAULT_PORT);
    }

    public boolean connect(String host, int port) {
        if (isConnected()) return false;
        return stream.connect(host, port);
    }

    public boolean disconnect() {
        if (!isConnected()) return false;
        remoteInputDispatcher.stop();
        remoteOutputDispatcher.stop();
        return stream.disconnect();
    }

    public boolean isConnected() {
        return stream.isConnected();
    }

    public void start() {
        if (!isConnected()) return;
        remoteInputDispatcher = new RemoteInputDispatcher(stream.getInputStream(), inbox);
        remoteOutputDispatcher = new RemoteOutputDispatcher(stream, outbox);
        remoteInputDispatcher.start();
        remoteOutputDispatcher.start();
    }

    public void stop() {
        if (!isConnected()) return;
        remoteInputDispatcher.stop();
        remoteOutputDispatcher.stop();
    }

    public byte[] serialize(@NotNull Message message) {
        message.setSessionId(currentSessionId);
        return serializer.serializeMessage(message);
    }

    public Message deserialize(@NotNull byte[] message) {
        return serializer.deserializeMessage(message);
    }

    public void checkConnection() {
        if (!isConnected()) {
            int i = 0;
            while (!connect(Config.IPS.get(i),Config.SERVER_DEFAULT_PORT)) {
                Logger.I(this, "Connecting to " + Config.IPS.get(i) + ":" + Config.SERVER_DEFAULT_PORT);
                if(i < Config.IPS.size()-1) {
                    i++;
                } else {
                    return;
                }
                Logger.E(this, "Server unavailable. Trying to reconnect");
            }
        }
    }
}

