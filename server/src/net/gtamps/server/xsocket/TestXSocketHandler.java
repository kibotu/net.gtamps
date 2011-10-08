package net.gtamps.server.xsocket;

import net.gtamps.server.MessageHandler;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xsocket.DataConverter;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.NonBlockingConnection;

/**
 * Basic connection handling: rudimentally parse incoming messages and notify
 * connection manager.
 * 
 * @author til
 * 
 */
public class TestXSocketHandler implements IDataHandler, IConnectHandler, IDisconnectHandler {
	private static final LogType TAG = LogType.SERVER;

	// This is where we will keep all active connections
	// private Set<INonBlockingConnection> sessions =
	// Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

	// ConnectionID, Connection
	private ConcurrentHashMap<String, INonBlockingConnection> connections =  new ConcurrentHashMap<String, INonBlockingConnection>();


	public TestXSocketHandler() {
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

	private String readByteToStringbyLength(INonBlockingConnection nbc, int len) {
		String s = null;
		while (s == null) {
			try {
				if (nbc.available() >= len) {
					s = nbc.readStringByLength(len);
				}
			} catch (BufferUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return s;

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
			System.out.println("expecting new message: " + laenge );
			//data = nbc.readStringByLength(laenge);
			byte[] data = new byte[laenge]; 
			this.readFully(nbc, data);
			this.receive(nbc, data);
			//data = readByteToStringbyLength(nbc, laenge);
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
		System.out.println(new String(data));
		send(nbc, data);
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
			connections.put(id, nbc);
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
	

	private void sendMsg(INonBlockingConnection nbc, String msg) {
		// Logger.i().log(TAG, "sending to " + nbc.getId());
		msg = msg.replace("\r\n", "\n");
		int length = msg.length();
		if (length < 1) {
			throw new IllegalArgumentException("bla!");
		}
		if (length > 65535) {
			// TODO
			return;
		}

		byte high = (byte) (length >> 8);
		byte low = (byte) (length & 0xFF);

		try {
			// TODO
			byte[] b = msg.getBytes();
			byte[] c = new byte[b.length + 2];
			c[0] = high;
			c[1] = low;
			for (int i = 0; i < b.length; i++) {
				c[i + 2] = b[i];
			}
			nbc.write(c);
			nbc.flush();
			// System.out.println(length + " bytes send:");
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
