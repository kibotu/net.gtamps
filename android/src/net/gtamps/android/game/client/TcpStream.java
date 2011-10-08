package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Config;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observer;

public class TcpStream implements IStream {

    private static final String TAG = TcpStream.class.getSimpleName();

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    /**
     *   @see <a href="http://stackoverflow.com/questions/2560083/how-to-change-internal-buffer-size-of-datainputstream">BufferSize</a>
      */
    public TcpStream() {
        socket = new Socket();
        try {
            socket.setSendBufferSize(Config.SOCKET_MAX_SEND_BUFFER_SIZE);
            socket.setReceiveBufferSize(Config.SOCKET_MAX_RECEIVE_BUFFER_SIZE);
            socket.setKeepAlive(Config.SOCKET_KEEP_ALIVE_ENABLED);
            socket.setTcpNoDelay(Config.SOCKET_TCP_NO_DELAY);
            socket.setSoTimeout(Config.SOCKET_TIMEOUT);
        } catch (SocketException e) {
            Utils.log(TAG, e.getMessage());
        }
    }

    @Override
    public boolean connect(String host, int port) {
        if(socket.isConnected()) return false;
        try {
            socket.connect(new InetSocketAddress(host, port));
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Utils.log(TAG, e.getMessage());
        }
        return socket.isConnected();
    }

    @Override
    public boolean disconnect() {
        if(!socket.isConnected()) return false;
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            Utils.log(TAG, e.getMessage());
        }
        return !socket.isConnected();
    }

    @Override
    public boolean send(String message) {
        if(!socket.isConnected()) return false;
        try {
            output.writeUTF(message);
            Utils.log(TAG, "has send string "+message.length());
        } catch (IOException e) {
            Utils.log(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean send(byte [] message) {
        if(!socket.isConnected()) return false;

        // TODO don't copy array and alloc byte array every sending invocation
        byte [] temp = new byte[message.length+2];
        temp[0] = (byte)(message.length >> 8);
        temp[1] = (byte) (message.length & 0xFF);
        System.arraycopy(message,0, temp, 2, message.length);
        try {
            output.write(temp);
            Utils.log(TAG, "has send bytes " +message.length);
        } catch (IOException e) {
            Utils.log(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public DataOutputStream getOutputStream() {
        return output;
    }

    @Override
    public DataInputStream getInputStream() {
        return input;
    }
}
