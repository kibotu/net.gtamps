package net.gtamps.server;

import net.gtamps.shared.serializer.helper.SerializedMessage;

import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;

public interface ISocketHandler extends IDataHandler, IConnectHandler, IDisconnectHandler {

	public void send(String connectionId, byte[] bytes, final int msgLength);

	public void send(String connectionId, byte[] bytes);

	public void send(String connectionId, SerializedMessage serMsg);

}
