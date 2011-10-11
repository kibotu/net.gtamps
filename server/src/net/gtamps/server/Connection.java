package net.gtamps.server;

import org.xsocket.connection.INonBlockingConnection;

import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;

public class Connection {
	private final ISocketHandler socketHandler;
	private final ISerializer serializer;
	private final INonBlockingConnection nbc;

	public Connection(INonBlockingConnection nbc, ISocketHandler socketHandler, ISerializer serializer) {
		if (socketHandler == null) {
			throw new IllegalArgumentException("'socketHandler' must not be null");
		}
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.socketHandler = socketHandler;
		this.serializer = serializer;
		this.nbc = nbc;
	}
	
	public void send(Message msg) {
		socketHandler.send(nbc, serializer.serializeMessage(msg));
	}
	

}
