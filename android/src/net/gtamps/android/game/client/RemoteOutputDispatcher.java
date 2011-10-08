package net.gtamps.android.game.client;


import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
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
        Utils.log(TAG, "Starts listening to socket runs.");
        isRunning = true;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while(isRunning) {
//            Thread.currentThread().sleep(33);
            if(outbox.isEmpty()) continue;
            Utils.log(TAG, "tries to send");
            tcpStream.send(MessageFactory.serialize(outbox.poll()));
        }
    }
}
