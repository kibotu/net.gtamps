package net.gtamps.shared.client;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.ObjectSerializer;
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
    private int currentSocketTimeOut = Config.MAX_SOCKET_TIMEOUT;

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
        return stream.connect(host, port);
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
        remoteInputDispatcher = new RemoteInputDispatcher(stream.getInputStream(), inbox);
        remoteOutputDispatcher = new RemoteOutputDispatcher(stream, outbox);
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

    public void checkConnection() {
        if(!isConnected()) {
            while (!connect()) {
                if(currentSocketTimeOut <= 0) System.exit(0);
                try {
                    Thread.sleep(Config.SOCKET_TIMEOUT);
                    currentSocketTimeOut -= Config.SOCKET_TIMEOUT;
                } catch (InterruptedException e) {
                }
                Logger.E(this, "Server unavailable. Trying to reconnect");
            }
        }
        currentSocketTimeOut = Config.MAX_SOCKET_TIMEOUT;
    }
}