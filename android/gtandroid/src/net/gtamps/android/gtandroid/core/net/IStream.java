package net.gtamps.android.gtandroid.core.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketException;

public interface IStream {

    public boolean send(String message);

    /**
     * Sends a message.
     *
     * @param message
     * @return <code>true</code> if succeeded
     * @throws java.net.SocketException
     */
    public boolean send(byte[] message) throws SocketException;

    /**
     * Sends a message.
     *
     * @param message
     * @return <code>true</code> if succeeded
     * @throws java.net.SocketException
     */
    public boolean send(byte[] buffer, int length) throws SocketException;

    /**
     * receives a message and returns its byte array
     * @return
     */
    public byte[] receive();
    
    /**
     * connects to a specific
     *
     * @param host ip address
     * @param port
     * @return <code>true</code> if succeeded
     */    
    public boolean connect(String host, int port);

    /**
     * Disconnects from socket.
     *
     * @return <code>true</code> if succeeded
     */
    public boolean disconnect();

    /**
     * Returns connection status.
     *
     * @return <code>true</code> if is connected
     */
    public boolean isConnected();

    public DataInputStream getInputStream();

    public DataOutputStream getOutputStream();
}
