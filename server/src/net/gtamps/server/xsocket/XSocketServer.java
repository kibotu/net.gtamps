package net.gtamps.server.xsocket;

import net.gtamps.server.MessageHandler;

import org.xsocket.connection.*;
 
public class XSocketServer implements Runnable
{
	private static IServer srv;
	private static int PORT = 8090;
	private static boolean isOnline = true;
	private static boolean wasStarted = false;
	
	MessageHandler messageHandler;
  
    public XSocketServer(MessageHandler messageHandler) {
		if (messageHandler == null) {
			throw new IllegalArgumentException("'connectionManager' must not be null");
		}
    	this.messageHandler = messageHandler;
	}

	public static void shutdownServer()
    {
        try
        {
        	System.out.println("Shutting down server!");
            srv.close();
            isOnline = false;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

	@Override
	public void run() {
		try
        {
            srv = new Server(PORT, new XSocketHandler(this.messageHandler));
            srv.run();
            isOnline = true;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
	}
	
	public boolean isOnline(){
		return isOnline;
	}
	
	public void stop(){
		if(wasStarted){
			shutdownServer();
			wasStarted = false;
		}
	}
	
	public void start(){
		if(!wasStarted){
			new Thread(this).start();
			wasStarted = true;
		}
	}
	
}
