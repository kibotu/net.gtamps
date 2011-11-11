package net.gtamps.server;

import net.gtamps.server.http.SimpleHttpServer;
import net.gtamps.server.xsocket.XSocketServer;

import java.io.FileNotFoundException;

public class ServerChainFactory {

    private static SimpleHttpServer httpServer = null;


    public static void startHTTPServer(final String path) {
        if (httpServer == null) {
            try {
                httpServer = new SimpleHttpServer(8080, path, false);
                new Thread(httpServer).start();
            } catch (final FileNotFoundException e) {
                System.out.println("Error starting HTTP Server:\n" + e);
            }
        }
    }

    public static void stopHTTPServer() {
        if (httpServer != null) {
            httpServer.stopServer();
        }
    }

    public static void createServerChain(final ISocketHandler handler) {
        new Thread(new XSocketServer(handler)).start();
    }

}
