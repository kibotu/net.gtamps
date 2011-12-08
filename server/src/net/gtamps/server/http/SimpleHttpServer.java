package net.gtamps.server.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer implements Runnable {

	private final int port;
    private final File documentDir;
    private final boolean enableLogging;
    private boolean isRunning = false;

    public SimpleHttpServer(final int port, final String directoryPath, final boolean enableLogging) throws FileNotFoundException {
        this.port = port;
        documentDir = new File(directoryPath);
        this.enableLogging = enableLogging;
        if (!documentDir.exists()) {
            throw new FileNotFoundException("No such document-dir exists: " + documentDir.getAbsolutePath());
        } else if (!documentDir.isDirectory()) {
            throw new FileNotFoundException("Document-dir " + documentDir.getAbsolutePath() + " is not a directory");
        }
    }

    @Override
    public void run() {
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            isRunning = true;
            try {
                while (isRunning) {
                    // wait for the next client to connect and get its
                    // socket connection
                    final Socket socket = serverSocket.accept();
                    // handle the socket connection by a handler in a new
                    // thread
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();
                    new Thread(new SimpleHttpServerHandler(socket, documentDir, enableLogging)).start();

                }
            } catch (final IOException e) {
                System.err
                        .println("Error while accepting connection on port "
                                + port);
            } finally {
                serverSocket.close();
            }
        } catch (final IOException e) {
            System.err.println("Failed to bind to port " + port);
        }
    }

    public void stopServer() {
        isRunning = false;
    }
    
    @Override
	public String toString() {
		return "SimpleHttpServer ["
			+ "port=" + port + ", "
			+ (documentDir != null ? "documentDir=" + documentDir + ", " : "")
//			+ "enableLogging=" + enableLogging
//			+ ", isRunning=" + isRunning
			+ "]";
	}

    
}
