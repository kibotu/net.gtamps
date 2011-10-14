package net.gtamps.server;

import org.xsocket.connection.INonBlockingConnection;

import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;

public class Connection {
	private final ISocketHandler socketHandler;
	private final ISerializer serializer;
	private final INonBlockingConnection nbc;
	private final MessageCenter msgCenter;

	public Connection(INonBlockingConnection nbc, ISocketHandler socketHandler, ISerializer serializer, MessageCenter msgCenter) {
		if (nbc == null) {
			throw new IllegalArgumentException("'nbc' must not be null");
		}
		if (socketHandler == null) {
			throw new IllegalArgumentException("'socketHandler' must not be null");
		}
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.socketHandler = socketHandler;
		this.serializer = serializer;
		this.nbc = nbc;
		this.msgCenter = msgCenter;
	}
	
	public void send(Message msg) {
		socketHandler.send(nbc, serializer.serializeMessage(msg));
	}
	
	public void onData(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("'bytes' must not be null");
		}
		msgCenter.receiveMessage(this, serializer.deserializeMessage(bytes));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nbc == null) ? 0 : nbc.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Connection other = (Connection) obj;
		if (nbc == null) {
			if (other.nbc != null) {
				return false;
			}
		} else if (!nbc.getId().equals(other.nbc.getId())) {
			return false;
		}
		return true;
	}
	
	
	

}
