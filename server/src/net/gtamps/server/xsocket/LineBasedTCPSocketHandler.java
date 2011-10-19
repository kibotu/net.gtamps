package net.gtamps.server.xsocket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ConcurrentHashMap;

import net.gtamps.server.Connection;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.ISerializer;

import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.NonBlockingConnection;

/**
 * Basic connection handling: rudimentally parse incoming messages and notify
 * connection manager.
 * 
 * @author til
 * 
 */
public class LineBasedTCPSocketHandler<S extends ISerializer> implements ISocketHandler {
	private static final LogType TAG = LogType.SERVER;

	// This is where we will keep all active connections
	// private Set<INonBlockingConnection> sessions =
	// Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

	// ConnectionID, Connection
	private ConcurrentHashMap<String, INonBlockingConnection> actualConnections =  new ConcurrentHashMap<String, INonBlockingConnection>();
	private final ConcurrentHashMap<String, Connection<?>> abstractConnections =  new ConcurrentHashMap<String, Connection<?>>();
	
	private final S serializer;

	public LineBasedTCPSocketHandler(S serializer) {
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.serializer = serializer;
	}

	@Override
	public boolean onData(INonBlockingConnection nbc) {
		int read = 0;
		byte[] data;
		try {
			data = nbc.readBytesByDelimiter("\n");
			this.receive(nbc, data);
			Logger.i().indicateNetworkReceiveActivity();
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("weird data");
		return true;
	}

	private void receive(INonBlockingConnection nbc, byte[] data) {
		//System.out.println(new String(data));
		//send(nbc, data)
		Connection<?> c = abstractConnections.get(nbc.getId());
		c.onData(data);
	}
	
	@Override
	public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
//		synchronized (connections) {
			String id = nbc.getId();
			System.out.println("Closed Connection: " + id);
			Logger.i().log(LogType.SERVER, "Closed Connection: " + id);
			abstractConnections.remove(id);
//		}
		return true;
	}

	@Override
	public boolean onConnect(INonBlockingConnection nbc) {
//		synchronized (connections) {
			// try {
			// nbc.write("Hello and welcome to the server!\n\0");
			// } catch (BufferOverflowException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			String id = nbc.getId();
			System.out.println("New Connection: " + id);
			Logger.i().log(LogType.SERVER, "New Connection! ip:" + nbc.getRemoteAddress() + " id:" + id);
			abstractConnections.put(id, new Connection<LineBasedTCPSocketHandler<S>>(nbc.getId(), this, serializer));
//		}
		return true;
	}

	public void send(String connectionId, byte[] bytes) {
		INonBlockingConnection nbc = actualConnections.get(connectionId);
		try {
			nbc.write(bytes);
			nbc.write((byte)0x0A);
			nbc.flush();
			System.out.println(bytes.length + " + 1 bytes send");
			// System.out.println(msg);
		} catch (BufferOverflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*private boolean isKnownConnection(INonBlockingConnection nbc) {
		synchronized (connections) {
			return connections.containsKey(nbc.getId());
		}
	}*/

	private String bytesToStr(byte[] b) {
		String s = "";
		for (int i = 0; i < b.length; i++) {
			s += (int) b[i] + " ";
		}
		return s;
	}
}
