package net.gtamps.server;

import java.io.FileNotFoundException;

import net.gtamps.server.http.SimpleHttpServer;
import net.gtamps.server.xsocket.XSocketServer;

public class ServerChainFactory {

    private static SimpleHttpServer httpServer = null;


    public static SimpleHttpServer startHTTPServer(final int port, final String path) {
        if (httpServer == null) {
            try {
                httpServer = new SimpleHttpServer(port, path, false);
                new Thread(httpServer).start();
            } catch (final FileNotFoundException e) {
                System.out.println("Error starting HTTP Server:\n" + e);
            }
        }
        return httpServer;
    }

    public static void stopHTTPServer() {
        if (httpServer != null) {
            httpServer.stopServer();
        }
    }

    public static XSocketServer createServerChain(final int port, final ISocketHandler handler) {
    	final XSocketServer server = new XSocketServer(port, handler);
        new Thread(server).start();
        return server;
    }

}
