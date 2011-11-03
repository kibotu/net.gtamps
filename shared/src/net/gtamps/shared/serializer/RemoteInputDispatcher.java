package net.gtamps.shared.serializer;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
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
        Logger.i(this, "Starts socket-listening loop.");
        isRunning = true;
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        byte[] response;
        int length;

        while (isRunning) {
            try {

                //  latency
                try {
                    Thread.sleep(Config.SOCKET_INBOX_LATENCY);
                } catch (InterruptedException e) {
                    Logger.e(this, e.getMessage());
                }

                // build message
                length = (inputStream.read() << 8) + inputStream.read();
                if (length < 0) continue;
                response = new byte[length];
                Logger.e(this, "has received " + length + " bytes");
                inputStream.readFully(response);
                Message message = ConnectionManager.deserialize(response);
                if (message != null) inbox.add(message);

            } catch (IOException e) {
                Logger.e(this, e.getMessage());
            } catch (NullPointerException e) {
                Logger.e(this, e.getMessage());
            }
        }
        Logger.i(this, "Stops socket-listening loop.");
    }
}