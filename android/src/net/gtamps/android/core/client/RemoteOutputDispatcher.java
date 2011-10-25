package net.gtamps.android.core.client;


import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.communication.Message;

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
        Logger.i(this, "Starts socket- writing loop.");
        isRunning = true;
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while(isRunning) {
            try {
                Thread.currentThread().sleep(Config.SOCKET_OUTBOX_LATENCY);
            } catch (InterruptedException e) {
                 Logger.i(this, e.getMessage());
            }
            if(outbox.isEmpty()) continue;
            tcpStream.send(ConnectionManager.serialize(outbox.poll()));
        }
        Logger.i(this,"Stops socket-listening loop.");
    }
}
