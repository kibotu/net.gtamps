package net.gtamps.server;

import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

public interface ISocketHandler extends IDataHandler, IConnectHandler, IDisconnectHandler {
	
	public void send(String connectionId, byte[] bytes);

}
