package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteInputDispatcher implements Runnable {

    private static final String TAG = RemoteInputDispatcher.class.getSimpleName();

    private volatile boolean isRunning;
    @Nullable
    private final DataInputStream inputStream;
    private final ConcurrentLinkedQueue<Message> inbox;

    public RemoteInputDispatcher(@Nullable DataInputStream inputStream, @NotNull ConcurrentLinkedQueue<Message> inbox) {
        isRunning = false;
        this.inputStream = inputStream;
        this.inbox = inbox;
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        Utils.log(TAG, "Starts socket-listening loop.");
        isRunning = true;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        byte [] response;
        int length;

        while(isRunning) {
            try {

                //  latency
                try {
                    Thread.sleep(Config.SOCKET_LATENCY);
                } catch (InterruptedException e) {
                     Utils.log(TAG, e.getMessage());
                }

                // build message
                length =  (inputStream.read() << 8) + inputStream.read();
                response = new byte[length];
                Utils.log(TAG, "has received "+ length + " bytes");
                inputStream.readFully(response);
                Message message = MessageFactory.deserialize(response);
                if(message != null) inbox.add(message);

            } catch (IOException e) {
                Utils.log(TAG, e.getMessage());
            } catch (NullPointerException e) {
                Utils.log(TAG, e.getMessage());
            }
        }
        Utils.log(TAG, "Stops socket-listening loop.");
    }
}