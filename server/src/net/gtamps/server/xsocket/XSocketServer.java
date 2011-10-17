package net.gtamps.server.xsocket;

import net.gtamps.server.ISocketHandler;
import net.gtamps.server.MessageCenter;
import net.gtamps.shared.communication.ISerializer;

import org.xsocket.connection.*;
 
public class XSocketServer implements Runnable
{
	private static IServer srv;
	private static int PORT = 8095;
	private static boolean isOnline = true;
	private static boolean wasStarted = false;
	
	private  ISerializer serializer;
	private  MessageCenter msgCenter;

	
	
//	MessageHandler messageHandler;
  
    public XSocketServer(ISerializer serializer) {
    	if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
    	if (msgCenter == null) {
			throw new IllegalArgumentException("'msgCenter' must not be null");
		}
    	this.serializer = serializer;
    	this.msgCenter = msgCenter;
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
            srv = new Server(PORT, new TestXSocketHandler(serializer));
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
