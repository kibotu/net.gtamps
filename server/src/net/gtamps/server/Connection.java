package net.gtamps.server;

import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;

public class Connection {
	private final ISocketHandler socketHandler;
	private final ISerializer serializer;

	public Connection(ISocketHandler socketHandler, ISerializer serializer) {
		if (socketHandler == null) {
			throw new IllegalArgumentException("'socketHandler' must not be null");
		}
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.socketHandler = socketHandler;
		this.serializer = serializer;
	}
	
	public void send(Message msg) {
		// TODO Auto-generated method stub
		
	}
	

}
