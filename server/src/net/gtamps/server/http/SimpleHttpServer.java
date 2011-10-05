package net.gtamps.server.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer implements Runnable{
	
	private int port;
	private File documentDir;
	private boolean enableLogging;
	private boolean isRunning = false;
	
	public SimpleHttpServer(int port, String directoryPath, boolean enableLogging ) throws FileNotFoundException {
			this.port = port;
			this.documentDir = new File(directoryPath);
			this.enableLogging = enableLogging;
			if (!documentDir.exists()) {
				throw new FileNotFoundException("No such document-dir exists: " + documentDir.getAbsolutePath());
			} else if (!documentDir.isDirectory()) {
				throw new FileNotFoundException("Document-dir " + documentDir.getAbsolutePath()	+ " is not a directory");
			}
	}

	@Override
	public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				this.isRunning = true;
				try {
					while (isRunning) {
						// wait for the next client to connect and get its
						// socket connection
						Socket socket = serverSocket.accept();
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
				} catch (IOException e) {
					System.err
							.println("Error while accepting connection on port "
									+ port);
				} finally {
					serverSocket.close();
				}
			} catch (IOException e) {
				System.err.println("Failed to bind to port " + port);
			}	
	}
	public void stopServer(){
		this.isRunning = false;
	}
}
