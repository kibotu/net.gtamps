package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteInputDispatcher implements Runnable {

    private static final String TAG = RemoteInputDispatcher.class.getSimpleName();

    private volatile boolean isRunning;
    @Nullable
    private DataInputStream inputStream;
    private ConcurrentLinkedQueue<Message> inbox;

    public RemoteInputDispatcher(DataInputStream inputStream, ConcurrentLinkedQueue<Message> inbox) {
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
        int length = 0;

        while(isRunning) {
            try {

                //  latency
                try {
                    Thread.currentThread().sleep(Config.SOCKET_LATENCY);
                } catch (InterruptedException e) {
                     Utils.log(TAG, e.getMessage());
                }

                // build message
                length = ((int)((byte) inputStream.read()) << 8) + ((int)((byte) inputStream.read() & 0xFF));
                response = new byte[length];
                Utils.log(TAG, "has received "+ length + " bytes");
                inputStream.readFully(response);
                Message message = MessageFactory.deserialize(response);
                if(message != null) inbox.add(message);

                /** var 2 **/
//                response = inputStream.readUTF().getBytes();
//                if(response != null && response.length > 1) {
//                    Utils.log(TAG, "has received "+length + " bytes");
//                    Message message = MessageFactory.deserialize(response);
//                }
            } catch (IOException e) {
                Utils.log(TAG, e.getMessage());
            }
        }
        Utils.log(TAG, "Stops socket-listening loop.");
    }
}