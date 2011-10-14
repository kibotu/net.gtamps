package net.gtamps.android.game.client;


import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.MessageFactory;

import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteOutputDispatcher extends Observable implements Runnable {

    private static final String TAG = RemoteOutputDispatcher.class.getSimpleName();

    private volatile boolean isRunning;
    private byte [] responseBytes;
    private byte [] response;
    private int length;
    private IStream tcpStream;
    private ConcurrentLinkedQueue<Message> outbox;

    public RemoteOutputDispatcher(IStream tcpStream, ConcurrentLinkedQueue<Message> outbox) {
        isRunning = false;
        responseBytes = new byte[2];
        this.tcpStream = tcpStream;
        this.outbox = outbox;
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        Utils.log(TAG, "Starts socket- writing loop.");
        isRunning = true;
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while(isRunning) {
            try {
                Thread.currentThread().sleep(Config.SOCKET_LATENCY);
            } catch (InterruptedException e) {
                 Utils.log(TAG, e.getMessage());
            }
            if(outbox.isEmpty()) continue;
            tcpStream.send(ConnectionManager.serialize(outbox.poll()));
        }
        Utils.log(TAG, "Stops socket-listening loop.");
    }
}
