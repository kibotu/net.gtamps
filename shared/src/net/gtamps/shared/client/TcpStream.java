package net.gtamps.shared.client;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TcpStream implements IStream {

    private static final String TAG = TcpStream.class.getSimpleName();

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    /**
     *   @see <a href="http://stackoverflow.com/questions/2560083/how-to-change-internal-buffer-size-of-datainputstream">BufferSize</a>
      */
    public TcpStream() {
        initSocket();
    }

    public void initSocket() {
        socket = new Socket();
        try {
            socket.setSendBufferSize(Config.SOCKET_MAX_SEND_BUFFER_SIZE);
            socket.setReceiveBufferSize(Config.SOCKET_MAX_RECEIVE_BUFFER_SIZE);
            socket.setKeepAlive(Config.SOCKET_KEEP_ALIVE_ENABLED);
            socket.setTcpNoDelay(Config.SOCKET_TCP_NO_DELAY);
            socket.setSoTimeout(Config.SOCKET_TIMEOUT);
//            socket.setReuseAddress(true);
        } catch (SocketException e) {
            Logger.printException(this, e);
        }
    }

    @Override
    public boolean connect(String host, int port) {
        initSocket();
        if(socket.isConnected()) return false;
        try {
            socket.connect(new InetSocketAddress(host, port));
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Logger.printException(this,e);
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
            Logger.printException(this, e);
        }
        return !socket.isConnected();
    }

    @Override
    public boolean send(String message) {
        if(!socket.isConnected()) return false;
        try {
            output.writeUTF(message);
            Logger.i(this, "has send string "+message.length());
        } catch (IOException e) {
            Logger.printException(this, e);
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
            Logger.i(this,"has send bytes " +message.length);
        } catch (IOException e) {
            Logger.printException(this, e);
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
