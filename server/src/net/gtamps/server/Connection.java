package net.gtamps.server;

import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;

/**
 * immutable!
 * 
 *
 */
public final class Connection<H extends ISocketHandler, S extends ISerializer> {
	private final H socketHandler;
	private final S serializer;
	private final String id;

	public Connection(String id, H socketHandler, S serializer) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (socketHandler == null) {
			throw new IllegalArgumentException("'socketHandler' must not be null");
		}
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.socketHandler = socketHandler;
		this.serializer = serializer;
		this.id = id;
	}
	
	public void send(Message msg) {
		socketHandler.send(id, serializer.serializeMessage(msg));
	}
	
	public void onData(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("'bytes' must not be null");
		}
		Message m = serializer.deserializeMessage(bytes);
		SessionManager.instance.receiveMessage(this, m);
		ControlCenter.instance.receiveMessage(this, m);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Connection<?,?> other = (Connection<?,?>) obj;
		if (this.socketHandler.getClass() != other.socketHandler.getClass()) {
			return false;
		}
		if (this.serializer.getClass() != other.serializer.getClass()) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	
	

}
