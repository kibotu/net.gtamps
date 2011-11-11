package net.gtamps.server;

import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;

public interface ISocketHandler extends IDataHandler, IConnectHandler, IDisconnectHandler {

    public void send(String connectionId, byte[] bytes);

}
