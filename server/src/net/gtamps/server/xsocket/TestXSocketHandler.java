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
import net.gtamps.server.MessageCenter;
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
public class TestXSocketHandler implements ISocketHandler {
	private static final LogType TAG = LogType.SERVER;

	// This is where we will keep all active connections
	// private Set<INonBlockingConnection> sessions =
	// Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

	// ConnectionID, Connection
	//private ConcurrentHashMap<String, INonBlockingConnection> connections =  new ConcurrentHashMap<String, INonBlockingConnection>();
	private ConcurrentHashMap<String, Connection> connections =  new ConcurrentHashMap<String, Connection>();
	private MessageCenter msgCenter;
	private ISerializer serializer;

	public TestXSocketHandler(MessageCenter msgCenter, ISerializer serializer) {
		if (msgCenter == null) {
			throw new IllegalArgumentException("'msgCenter' must not be null");
		}
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.msgCenter = msgCenter;
		this.serializer = serializer;
	}

	private byte readByte(INonBlockingConnection nbc) {
		Byte b = null;
		while (b == null) {
			try {
				if (nbc.available() > 0) {
					b = nbc.readByte();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;

	}

	private void readFully(INonBlockingConnection nbc, byte[] data) throws IOException {
		assert nbc != null;
		assert data != null;
		int need = data.length;
		int read = 0;
		while (need > 0) {
			int offered = Math.min(nbc.available(), need);
			ByteBuffer[] buffers = null;
			try {
				buffers = nbc.readByteBufferByLength(offered);
			} catch (BufferUnderflowException bue) {
				if (nbc.isOpen()) {
					try {
						Thread.sleep(50l);
					} catch (InterruptedException e) {
						// we're fine.
					}
				} else {
					throw new ClosedChannelException();
				}
			}
			if (buffers == null) {
				continue;
			}
			for (ByteBuffer b : buffers) {
				if (b == null) {
					continue;
				}
				int has = b.remaining();
				b.get(data, read, has);
				read += has;
				need -= has;
			}
		}

//		t[0].ge t(dst, offset, length)
//		nbc.readBytesByLength(laenge);
	}
	
	@Override
	public boolean onData(INonBlockingConnection nbc) {
		int read = 0;
		try {

			byte hi = readByte(nbc);
			byte lo = readByte(nbc);
			read += 2;
			
			int laenge = ((((int) hi) & 0xff) << 8) + (((int) lo)  & 0xff);
			//System.out.println("expecting new message: " + laenge );
			byte[] data = new byte[laenge]; 
			this.readFully(nbc, data);
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
		Connection c = connections.get(nbc.getId());
		c.onData(data);
	}
	
	@Override
	public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
//		synchronized (connections) {
			String id = nbc.getId();
			System.out.println("Closed Connection: " + id);
			Logger.i().log(LogType.SERVER, "Closed Connection: " + id);
			connections.remove(id);
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
			connections.put(id, new Connection(nbc, this, serializer, msgCenter));
//		}
		return true;
	}

	public void send(INonBlockingConnection nbc, byte[] bytes) {
		int length = bytes.length;
		if (length < 1) {
			return;
		}
		if (length > 65535) {
			throw new IllegalArgumentException("bla!");
		}

		byte high = (byte) (length >> 8);
		byte low = (byte) (length & 0xFF);

		try {
			//NonBlockingConnection ncv;
			//ncv.write(buffers);
			nbc.write(high);
			nbc.write(low);
			nbc.write(bytes);
			nbc.flush();
			System.out.println(length + " + 2 bytes send");
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
