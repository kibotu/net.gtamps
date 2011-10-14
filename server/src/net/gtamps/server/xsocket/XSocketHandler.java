package net.gtamps.server.xsocket;

//import net.gtamps.server.MessageHandler;
//import net.gtamps.server.gui.LogType;
//import net.gtamps.server.gui.Logger;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.io.StringReader;
//import java.io.UnsupportedEncodingException;
//import java.nio.BufferOverflowException;
//import java.nio.BufferUnderflowException;
//import java.nio.channels.ClosedChannelException;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;
//import org.jdom.output.XMLOutputter;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import org.xsocket.MaxReadSizeExceededException;
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
public class XSocketHandler implements IDataHandler, IConnectHandler, IDisconnectHandler {

	@Override
	public boolean onDisconnect(INonBlockingConnection connection)
			throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConnect(INonBlockingConnection connection)
			throws IOException, BufferUnderflowException,
			MaxReadSizeExceededException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onData(INonBlockingConnection connection)
			throws IOException, BufferUnderflowException,
			ClosedChannelException, MaxReadSizeExceededException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
/*public class XSocketHandler implements IDataHandler, IConnectHandler, IDisconnectHandler {
	private static final LogType TAG = LogType.SERVER;

	// This is where we will keep all active connections
	// private Set<INonBlockingConnection> sessions =
	// Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

	// ConnectionID, Connection
	private ConcurrentHashMap<String, INonBlockingConnection> connections =  new ConcurrentHashMap<String, INonBlockingConnection>();

	private final XMLOutputter xmlOutputter;
	private final SAXBuilder saxBuilder;
//	private final MessageHandler messageHandler;

	private final String policyRequest = "<policy-file-request/>\u0000";
	private final String policyString = "<?xml version=\"1.0\"?>"
			+ "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">" + "<cross-domain-policy>"
			+ "<site-control permitted-cross-domain-policies=\"all\"/>"
			+ "<allow-access-from domain=\"*\" to-ports=\"*\" />" + "</cross-domain-policy>";

	public XSocketHandler(MessageHandler messageHandler) {
		if (messageHandler == null) {
			throw new IllegalArgumentException("'connectionManager' must not be null");
		}
		this.xmlOutputter = new XMLOutputter();
		this.saxBuilder = new SAXBuilder();
		this.messageHandler = messageHandler;
		messageHandler.setSocketHandler(this);
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

	@Override
	public boolean onData(INonBlockingConnection nbc) {
		// System.out.println(nbc.getId() + " data.");
		String data = "";
		byte[] bytes = new byte[2];
		int read = 0;
		try {

			bytes[0] = readByte(nbc);
			bytes[1] = readByte(nbc);

			read += 2;
			
			if (bytes[0] == 60 && bytes[1] == 112) {
				data = "" + (char) bytes[0] + (char) bytes[1];
				
				//data += nbc.readStringByLength(policyRequest.length() - read);
				data+= readByteToStringbyLength(nbc, policyRequest.length() - read);
				read += policyRequest.length() - read;
				// System.out.println(data.toString());
				if (data.equals(policyRequest)) {
					return handlePolicyRequest(nbc);
				}
			}
			int laenge = ((int) (bytes[0])) * 256 + (int) (bytes[1]);
			//data = nbc.readStringByLength(laenge);
			data = readByteToStringbyLength(nbc, laenge);
			Logger.i().indicateNetworkReceiveActivity();
			return handleStandardRequest(nbc, data);
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

	@Override
	public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
//		synchronized (connections) {
			String id = nbc.getId();
			System.out.println("Closed Connection: " + id);
			Logger.i().log(LogType.SERVER, "Closed Connection: " + id);
			connections.remove(id);
			messageHandler.removeConnection(id);
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
			messageHandler.addConnection(id);
//		}

		return true;
	}

	private boolean handleStandardRequest(INonBlockingConnection nbc, String data) throws IOException {
		// System.out.println(nbc.getId() + ": " + data);
		Reader in = new StringReader(data);
		try {
			Document doc;
			doc = saxBuilder.build(in);
			this.messageHandler.receive(nbc.getId(), doc.getRootElement());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.err.println(e);
		}
		return true;
	}

	private boolean handlePolicyRequest(INonBlockingConnection nbc) throws BufferOverflowException, IOException {
		String message = policyString + "\u0000";
		while (nbc.available() > 0) {
			nbc.readBytesByLength(nbc.available());
		}
		nbc.write(message);
		nbc.flush();
		Logger.i().log(LogType.SERVER, "served policy file to " + nbc.getId());
		System.out.println("served policy file to " + nbc.getId());
		return true;
	}

	*//**
	 * Wraps <code>message</code> element in an xml document, and writes it in
	 * String form to the connection with <code>connectionId</code>.
	 * 
	 * @param connectionId
	 * @param message
	 * @throws ClosedChannelException
	 *//*
	public void send(String connectionID, Element message) throws ClosedChannelException {
		if (message == null) {
			return;
		}
		INonBlockingConnection nbc;
//		synchronized (connections) {
			nbc = connections.get(connectionID);
//		}
		Logger.i().indicateNetworkSendActivity();
		if (nbc == null) {
			throw new ClosedChannelException();
		}
		{// TODO
			Logger.i().indicateNetworkSendActivity();
			Document doc = new Document(message);
			this.sendMsg(nbc, xmlOutputter.outputString(doc).replaceFirst("<\\?[^?>]*\\?>", ""));
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

	private String bytesToStr(byte[] b) {
		String s = "";
		for (int i = 0; i < b.length; i++) {
			s += (int) b[i] + " ";
		}
		return s;
	}
}
*/