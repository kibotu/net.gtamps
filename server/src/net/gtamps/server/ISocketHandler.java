package net.gtamps.server;

import java.nio.channels.ClosedChannelException;

import org.xsocket.connection.IHandler;

public interface ISocketHandler extends IHandler {
	
	public void send(String connectionId, byte[] bytes) throws ClosedChannelException;
//	public void onData(ByteBuffer dataSource);

}
