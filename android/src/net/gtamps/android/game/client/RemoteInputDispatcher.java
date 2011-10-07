package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteInputDispatcher implements Runnable {

    private static final String TAG = RemoteInputDispatcher.class.getSimpleName();

    private volatile boolean isRunning;
    private byte [] responseBytes;
    private byte [] response;
    private int length;
    @Nullable
    private DataInputStream inputStream;
    ConcurrentLinkedQueue<Message> inbox;

    public RemoteInputDispatcher(DataInputStream inputStream, ConcurrentLinkedQueue<Message> inbox) {
        isRunning = false;
        responseBytes = new byte[2];
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
        Utils.log(TAG, "Starts listening to socket runs.");
        isRunning = true;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while(isRunning) {
            try {
                /** var 1 **/
//                responseBytes[0] = (byte) socket.getInputStream().read();
//                responseBytes[1] = (byte) socket.getInputStream().read();
                responseBytes[0] = (byte) inputStream.read();
                responseBytes[1] = (byte) inputStream.read();
                length = ((int)(responseBytes[0]) << 8) + ((int)(responseBytes[1] & 0xFF));
                response = new byte[length];
                inputStream.readFully(response);
                parseMessage(response);
                /** var 2 **/
    //                        response = input.readUTF().getBytes();
    //                        if(response.length > 1) {
    //                            onData(response);
    //                        }
            } catch (IOException e) {
                Utils.log(TAG, ""+e.getMessage());
            }
        }
    }

    private void parseMessage(byte[] response) {
        // TODO parse message
        inbox.add(MessageFactory.create(response));
    }
}