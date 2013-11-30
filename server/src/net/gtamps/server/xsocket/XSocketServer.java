package net.gtamps.server.xsocket;

import java.net.InetAddress;

import net.gtamps.server.ISocketHandler;

import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

public class XSocketServer implements Runnable {

	private static IServer srv;
    private static boolean isOnline = true;
    private static boolean wasStarted = false;

    private final ISocketHandler socketHandler;
    private final int port;

//	MessageHandler messageHandler;

    public XSocketServer(final int port, final ISocketHandler socketHandler) {
        if (socketHandler == null) {
            throw new IllegalArgumentException("'serializer' must not be null");
        }
        this.port = port;
        this.socketHandler = socketHandler;
    }

    public static void shutdownServer() {
        try {
            System.out.println("Shutting down server!");
            srv.close();
            isOnline = false;
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            srv = new Server(port, socketHandler);
            srv.run();
            isOnline = true;
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void stop() {
        if (wasStarted) {
            shutdownServer();
            wasStarted = false;
        }
    }

    public void start() {
        if (!wasStarted) {
            new Thread(this).start();
            wasStarted = true;
        }
    }
    
    public InetAddress getLocalAddress() {
    	return srv.getLocalAddress();
    }
    
    public String getLocalIPAddress() {
    	return srv.getLocalAddress().getHostAddress();
    }
    
    public int getLocalPort() {
    	return srv.getLocalPort();
    }
    
    @Override
	public String toString() {
		return "XSocketServer ["
		+ "port=" + port + ", "
		+ (socketHandler != null ? "socketHandler=" + socketHandler  : "")
		+ "]";
	}
}
