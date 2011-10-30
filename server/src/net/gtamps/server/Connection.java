package net.gtamps.server;

import org.xsocket.IDataSink;
import org.xsocket.IDataSource;


/**
 * immutable!
 * 
 *
 */
public final class Connection<H extends ISocketHandler> implements IDataSink {
//	private final H socketHandler;
//	private final ISerializer serializer;

	
	private final String id;
	private final IDataSink dataSink;
	private final IDataSource dataSource;
	
	//	private final String host;
//	private final int port;

	public Connection(final String id, final IDataSink dataSink, final IDataSource dataSource) {
		this.id = id;
		this.dataSink = dataSink;
		this.dataSource = dataSource;
	}
	
	
	
	
//	public Connection(final String id, final H socketHandler, final ISerializer serializer) {
//		if (id == null) {
//			throw new IllegalArgumentException("'id' must not be null");
//		}
//		if (socketHandler == null) {
//			throw new IllegalArgumentException("'socketHandler' must not be null");
//		}
//		if (serializer == null) {
//			throw new IllegalArgumentException("'serializer' must not be null");
//		}
//		this.socketHandler = socketHandler;
//		this.serializer = serializer;
//		this.id = id;
//	}
	
//	public void send(final Message msg) {
//		final byte[] bytes = serializer.serializeMessage(msg);
//		socketHandler.send(id, bytes);
//	}
//
//	public void onData(final byte[] bytes) {
//		if (bytes == null) {
//			throw new IllegalArgumentException("'bytes' must not be null");
//		}
//		final Message m = serializer.deserializeMessage(bytes);
//		//SessionManager.instance.receiveMessage(this, m);
//		ControlCenter.instance.receiveMessage(this, m);
//	}

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
		if (socketHandler.getClass() != other.socketHandler.getClass()) {
			return false;
		}
		if (serializer.getClass() != other.serializer.getClass()) {
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
