package net.gtamps.server.xsocket;

import net.gtamps.server.ISocketHandler;
import net.gtamps.shared.communication.ISerializer;

import org.xsocket.connection.*;
 
public class XSocketServer implements Runnable
{
	private static IServer srv;
	private static int PORT = 8095;
	private static boolean isOnline = true;
	private static boolean wasStarted = false;
	
	private  ISocketHandler socketHandler;
	
	
//	MessageHandler messageHandler;
  
    public XSocketServer(ISocketHandler socketHandler) {
    	if (socketHandler == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
    	this.socketHandler = socketHandler;
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
            srv = new Server(PORT, this.socketHandler);
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
