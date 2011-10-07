package net.gtamps.android.game.client;

import android.text.Html;
import net.gtamps.android.core.utils.Utils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class TcpStream implements IStream {

    private static final String TAG = TcpStream.class.getSimpleName();

    private boolean isConnected;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private RemoteInputDispatcher dispatcher;
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
        isConnected = false;
    }

    @Override
    public boolean connect(String host, int port) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            socket.connect(address);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            dispatcher = new RemoteInputDispatcher(input);
        } catch (IOException e) {
            return isConnected = false;
        }
        return isConnected = true;
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
            dispatcher.stop();
        } catch (IOException e) {
            return isConnected = false;
        }
        return isConnected = true;
    }

    public void addStreamInputListener(StreamInputListener lister) {
        dispatcher.addObserver(lister);
    }

    public void removeStreamInputListener(StreamInputListener lister) {
        dispatcher.deleteObserver(lister);
    }

    @Override
    public void start() {
        dispatcher.start();
    }

    @Override
    public void stop() {
        dispatcher.stop();
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }
}
