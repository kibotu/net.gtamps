package net.gtamps.server;

import java.io.FileNotFoundException;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.server.http.SimpleHttpServer;
import net.gtamps.server.xsocket.LengthEncodedTCPSocketHandler;
import net.gtamps.server.xsocket.XSocketHandler;
import net.gtamps.server.xsocket.XSocketServer;
import net.gtamps.shared.communication.ISerializer;

public class ServerChainFactory {
	
	private static SimpleHttpServer httpServer = null;
	
	public static ConnectionManager createServerChain() {
		// hither:
		ConnectionManager connectionManager = new ConnectionManager();
//		Logger.i().log(LogType.SERVER, "Started ConnectionManager...");
//		CommandHandler	commandHandler = new CommandHandler(connectionManager);
//		Logger.i().log(LogType.SERVER, "Started CommandHandler...");
//		RequestHandler requestHandler = new RequestHandler(connectionManager);
//		Logger.i().log(LogType.SERVER, "Started RequestHandler...");
//		MessageHandler messageHandler = new MessageHandler(commandHandler, requestHandler);
//		Logger.i().log(LogType.SERVER, "Started MessageHandler...");
//		new Thread(new XSocketServer(messageHandler)).start();
//		Logger.i().log(LogType.SERVER, "Started XSocketServer...");

		// wither:
		// handled in Constructors
	//	new Thread(new XSocketServer()).start();
		return connectionManager;
		
	}
	
	public static void startHTTPServer(String path){
		if(httpServer == null){
			try {
				httpServer = new SimpleHttpServer(8080, path, false);
				new Thread(httpServer).start();
			} catch (FileNotFoundException e) {
				System.out.println("Error starting HTTP Server:\n"+e);
			}
		}
	}
	public static void stopHTTPServer(){
		if(httpServer != null){
			httpServer.stopServer();
		}
	}

	public static void createServerChainII(ISocketHandler handler) {
		ISerializer serializer = new ObjectSerializer();
		new Thread(new XSocketServer(handler)).start();
	}

}
