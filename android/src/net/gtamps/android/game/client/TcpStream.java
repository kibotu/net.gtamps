package net.gtamps.android.game.client;

import android.text.Html;
import net.gtamps.android.core.utils.Utils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class TcpStream implements IStream {

    private static final String TAG = TcpStream.class.getSimpleName();

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    /**
     *   @see <a href="http://stackoverflow.com/questions/2560083/how-to-change-internal-buffer-size-of-datainputstream">BufferSize</a>
      */
    public static final int MAX_SEND_BUFFER_SIZE = 1536;
    public static final int MAX_RECEIVE_BUFFER_SIZE = 1536;
    public static final boolean TCP_NO_DELAY = true;
    public static final int TIMEOUT = 3000;

    public TcpStream() {
        socket = new Socket();
        try {
            socket.setSendBufferSize(MAX_SEND_BUFFER_SIZE);
            socket.setReceiveBufferSize(MAX_RECEIVE_BUFFER_SIZE);
            socket.setTcpNoDelay(TCP_NO_DELAY);
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            Utils.log(TAG, "" + e.getMessage());
        }
    }

    @Override
    public boolean connect(String host, int port) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            socket.connect(address);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean send(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean send(byte [] message) {

        // TODO don't copy array and alloc byte array every sending invocation
        byte [] temp = new byte[message.length+2];
        temp[0] = (byte)(message.length >> 8);
        temp[1] = (byte) (message.length & 0xFF);
        System.arraycopy(message,0, temp, 2, message.length);

        try {
            output.write(temp);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean disconnect() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onData(byte[] response) {
//        if(response.length > 1) {
            Utils.log(TAG, "\n\n\n\n\nResponse "+ new String(response)+"\n\n\n\n\n");
//        }
    }

    public void startListening() {
        Utils.log(TAG, "client listens");
        new Thread(new Runnable() {
            boolean isRunning = false;
            byte [] responseBytes = new byte[2];
            byte [] response;
            int length;
            int actualReadBytes = 0;
            int lastReadBytes = 0;
            boolean isLocked = false;
            @Override
            public void run() {
                Utils.log(TAG, "client runs");
                isRunning = true;
                while(true) {
                    try {
                        /** var 1 **/
                        responseBytes[0] = (byte) socket.getInputStream().read();
                        responseBytes[1] = (byte) socket.getInputStream().read();
                        length = ((int)(responseBytes[0]) << 8) + ((int)(responseBytes[1] & 0xFF));
                        response = new byte[length];
                        input.readFully(response);
                        onData(response);
                        /** var 2 **/
//                        response = input.readUTF().getBytes();
//                        if(response.length > 1) {
//                            onData(response);
//                        }
                    } catch (IOException e) {
                        Utils.log(TAG, ""+e.getMessage());
                    }

//                    try {
//                        if(isLocked) {
//                            actualReadBytes = input.read(response);
//                            if(actualReadBytes == length) {
//
//                                // fire event
//                                onData(response);
//                                // reset
//                                isLocked = false;
//                                lastReadBytes = 0;
//                                actualReadBytes = 0;
//                                length = 0;
//                            }
//                        } else {
//                            // find response length
//                            responseBytes[0] = (byte) socket.getInputStream().read();
//                            responseBytes[1] = (byte) socket.getInputStream().read();
//                            length = ((int)(responseBytes[0]) << 8) + ((int)(responseBytes[1] & 0xFF));
//                            response = new byte[length];
//                            Utils.log(TAG, "server runs, message length: " + length);
//                            // lock to current message
//                            isLocked = true;
//                        }
//                    } catch (IOException e) {
//                        Utils.log(TAG, ""+e.getMessage());
//                    }
                }
            }
        }).start();
    }
}
