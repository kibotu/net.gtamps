package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionManager implements IMessageManager {

    private static final String TAG = ConnectionManager.class.getSimpleName();

    private final ConcurrentLinkedQueue<Message> inbox;
    private final ConcurrentLinkedQueue<Message> outbox;
    private IStream stream;
    private RemoteInputDispatcher remoteInputDispatcher;
    private RemoteOutputDispatcher remoteOutputDispatcher;

    public ConnectionManager() {
        stream = new TcpStream();
        inbox = new ConcurrentLinkedQueue<Message>();
        outbox = new ConcurrentLinkedQueue<Message>();
    }

    @Override
    public boolean isEmpty() {
        return inbox.isEmpty();
    }

    @Override
    public Message poll() {
        return inbox.poll();
    }

    @Override
    public boolean add(@NotNull Message message) {
        return outbox.add(message);
    }

    public void update(Observable observable, Object o) {
//        if(response.length > 1) {
        Utils.log(TAG, "\n\n\n\n\nResponse " + new String(o.toString()) + "\n\n\n\n\n");
//    }
        if(o instanceof Message) {
            inbox.add(parseMessage(o));
        }
    }

    private Message parseMessage(Object o) {
        return new Message();
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
}