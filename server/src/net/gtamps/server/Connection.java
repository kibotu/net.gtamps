package net.gtamps.server;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.helper.FileDumper;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.helper.SerializedMessage;

/**
 * immutable!
 */
public final class Connection<H extends ISocketHandler> {
	private final H socketHandler;
	private final ISerializer serializer;
	private final String id;
	private final ControlCenter controlCenter = ControlCenter.instance;
	private boolean isActive;

	public Connection(final String id, final H socketHandler, final ISerializer serializer) {
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
		this.isActive = true;
	}

	public void send(final NewMessage msg) {
		if(this.isActive){
			final SerializedMessage serializedMessage = serializer.serializeAndPackNewMessage(msg);
			if(GTAMultiplayerServer.WRITE_SERIALIZED_MESSAGE_DUMPS){
				FileDumper.writeBytesToFile(id, serializedMessage.message,serializedMessage.length);
			}
			socketHandler.send(id, serializedMessage);
		} else {
			System.out.println("Handle of client request on closed connection, on ID: "+id);
		}
	}

	public void onData(final byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("'bytes' must not be null");
		}
		final NewMessage m = serializer.deserializeNewMessage(bytes);
		controlCenter.receiveMessage(this, m);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Connection<?> other = (Connection<?>) obj;
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

	public void setActive(final boolean active){
		this.isActive = active;
	}

}
