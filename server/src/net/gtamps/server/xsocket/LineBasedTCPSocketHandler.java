package net.gtamps.server.xsocket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ConcurrentHashMap;

import net.gtamps.server.Connection;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.serializer.communication.ISerializer;

import org.xsocket.connection.INonBlockingConnection;

/**
 * Basic connection handling: rudimentally parse incoming messages and notify
 * connection manager.
 * 
 * @author til
 * 
 */
public class LineBasedTCPSocketHandler<S extends ISerializer> implements ISocketHandler {
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

	public LineBasedTCPSocketHandler(final S serializer) {
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.serializer = serializer;
	}

	@Override
	public boolean onData(final INonBlockingConnection nbc) {
		byte[] data;
		try {
			data = nbc.readBytesByDelimiter("\n");
			this.receive(nbc, data);
			Logger.i().indicateNetworkReceiveActivity();
			nbc.write(PROMPT);
			nbc.flush();
		} catch (final ClosedChannelException e) {
				disconnect(nbc);
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		// System.out.println("weird data");
		return true;
	}

	private void receive(final INonBlockingConnection nbc, final byte[] data) {
		final Connection<?> c = abstractConnections.get(nbc.getId());
		c.onData(data);
	}
	
	@Override
	public boolean onDisconnect(final INonBlockingConnection nbc) throws IOException {
		disconnect(nbc);
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
//		}
		return true;
	}

	@Override
	public void send(final String connectionId, final byte[] bytes) {
		final INonBlockingConnection nbc = actualConnections.get(connectionId);
		try {
			nbc.write(RETURN);
			nbc.write(bytes);
			nbc.write((byte)0x0A);
			nbc.flush();
			System.out.println(bytes.length + " + 1 bytes send");
			// System.out.println(msg);
		} catch (final BufferOverflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void connect(final INonBlockingConnection nbc) {
		final String id = nbc.getId();
		System.out.println("New Connection: " + id);
		Logger.i().log(LogType.SERVER, "New Connection! ip:" + nbc.getRemoteAddress() + " id:" + id);
		abstractConnections.put(id, new Connection<LineBasedTCPSocketHandler<S>>(nbc.getId(), this, serializer));
		actualConnections.put(id, nbc);
	}
	
	private void disconnect(final INonBlockingConnection nbc) {
		final String id = nbc.getId();
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
