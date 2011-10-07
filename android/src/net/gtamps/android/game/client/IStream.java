package net.gtamps.android.game.client;

public interface IStream {

    public boolean send(String message);

    /**
     * Sends a message.
     *
     * @param message
     * @return <code>true</code> if succeeded
     */
    public boolean send(byte[] message);

    /**
     * connects to a specific
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
     * Starts listening thread.
     */
    public void start();

    /**
     * Stops listening thread.
     */
    public void stop();

    /**
     * Returns connection status.
     *
     * @return <code>true</code> if is connected
     */
    public boolean isConnected();
}
