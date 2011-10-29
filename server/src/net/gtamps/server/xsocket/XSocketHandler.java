package net.gtamps.server.xsocket;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ConcurrentHashMap;

import net.gtamps.server.Connection;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.ISerializer;

import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

	/**
	 * Basic connection handling: rudimentally parse incoming messages and notify
	 * connection manager.
	 * 
	 * @author til
	 * 
	 */
	public abstract class XSocketHandler<S extends ISerializer> implements ISocketHandler, IDataHandler, IConnectHandler, IDisconnectHandler {

		private static final LogType TAG = LogType.SERVER;
		
		private static final String PROMPT = "> ";
		private static final String RETURN = "< ";

		// This is where we will keep all active connections
		// private Set<INonBlockingConnection> sessions =
		// Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

		// ConnectionID, Connection
		private final ConcurrentHashMap<String, INonBlockingConnection> actualConnections =  new ConcurrentHashMap<String, INonBlockingConnection>();
		private final ConcurrentHashMap<String, Connection<?>> abstractConnections =  new ConcurrentHashMap<String, Connection<?>>();
		
		private final S serializer;

		public XSocketHandler(final S serializer) {
			if (serializer == null) {
				throw new IllegalArgumentException("'serializer' must not be null");
			}
			this.serializer = serializer;
		}
		

		@Override
		public boolean onData(final INonBlockingConnection nbc) {
			final byte[] data = receiveData(nbc);
			this.receive(nbc, data);
			return true;
		}

		protected abstract byte[] receiveData(INonBlockingConnection nbc);
		
		private void receive(final INonBlockingConnection nbc, final byte[] data) {
			final Connection<?> c = abstractConnections.get(nbc.getId());
			c.onData(data);
		}
		
		@Override
		public boolean onDisconnect(final INonBlockingConnection nbc) throws IOException {
			disconnect(nbc.getId());
			return true;
		}

		@Override
		public boolean onConnect(final INonBlockingConnection nbc) {
			connect(nbc);
				try {
					nbc.write(PROMPT);
				} catch (final BufferOverflowException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//			}
			return true;
		}

		@Override
		public void send(final String connectionId, final byte[] bytes) throws ClosedChannelException {
			final INonBlockingConnection nbc = actualConnections.get(connectionId);
			if (nbc == null) {
				disconnect(connectionId);
				throw new ClosedChannelException();
			}
			sendData(nbc, bytes);
		}
		
		protected abstract void sendData(INonBlockingConnection nbc, byte[] data);

		private void connect(final INonBlockingConnection nbc) {
			final String id = nbc.getId();
			System.out.println("New Connection: " + id);
			Logger.i().log(LogType.SERVER, "New Connection! ip:" + nbc.getRemoteAddress() + " id:" + id);
			abstractConnections.put(id, new Connection<XSocketHandler<S>>(nbc.getId(), this, serializer));
			actualConnections.put(id, nbc);
		}
		
		private void disconnect(final String connectionId) {
			final String id = connectionId;
			System.out.println("Closed Connection: " + id);
			Logger.i().log(LogType.SERVER, "Closed Connection: " + id);
			abstractConnections.remove(id);
			actualConnections.remove(id);
		}
		
		private String bytesToStr(final byte[] b) {
			String s = "";
			for (int i = 0; i < b.length; i++) {
				s += (int) b[i] + " ";
			}
			return s;
		}
	}
