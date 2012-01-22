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
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.helper.BinaryConverter;
import net.gtamps.shared.serializer.helper.SerializedMessage;

import org.jetbrains.annotations.NotNull;
import org.xsocket.connection.INonBlockingConnection;

/**
 * Basic connection handling: rudimentally parse incoming messages and notify
 * connection manager.
 *
 * @author til
 */
public class LengthEncodedTCPSocketHandler<S extends ISerializer> implements ISocketHandler {
	private static final LogType TAG = LogType.SERVER;

	// This is where we will keep all active connections
	// private Set<INonBlockingConnection> sessions =
	// Collections.synchronizedSet(new HashSet<INonBlockingConnection>());

	// ConnectionID, Connection
	private final ConcurrentHashMap<String, INonBlockingConnection> actualConnections = new ConcurrentHashMap<String, INonBlockingConnection>();
	private final ConcurrentHashMap<String, Connection<?>> abstractConnections = new ConcurrentHashMap<String, Connection<?>>();

	private final S serializer;

	public LengthEncodedTCPSocketHandler(final S serializer) {
		if (serializer == null) {
			throw new IllegalArgumentException("'serializer' must not be null");
		}
		this.serializer = serializer;
	}

	private byte readByte(final INonBlockingConnection nbc) {
		Byte b = null;
		while (b == null) {
			try {
				if (nbc.available() > 0) {
					b = nbc.readByte();
				}
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO: better solution
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;

	}

	private void readFully(final INonBlockingConnection nbc, final byte[] data, final int length) throws IOException {
		assert nbc != null;
		assert data != null;
		int need = length;
		int read = 0;
		while (need > 0) {
			final int offered = Math.min(nbc.available(), need);
			ByteBuffer[] buffers = null;
			try {
				buffers = nbc.readByteBufferByLength(offered);
			} catch (final BufferUnderflowException bue) {
				if (nbc.isOpen()) {
					try {
						Thread.sleep(50l);
					} catch (final InterruptedException e) {
						// we're fine.
					}
				} else {
					throw new ClosedChannelException();
				}
			}
			if (buffers == null) {
				continue;
			}
			for (final ByteBuffer b : buffers) {
				if (b == null) {
					continue;
				}
				final int has = b.remaining();
				b.get(data, read, has);
				read += has;
				need -= has;
			}
		}

		//		t[0].ge t(dst, offset, length)
		//		nbc.readBytesByLength(laenge);
	}

	@Override
	public boolean onData(final INonBlockingConnection nbc) {
		int read = 0;
		try {
			final byte[] msgLen = new byte[4];
			msgLen[0] = readByte(nbc);
			msgLen[1] = readByte(nbc);
			msgLen[2] = readByte(nbc);
			msgLen[3] = readByte(nbc);
			read += 4;

			final int laenge = BinaryConverter.readIntFromBytes(msgLen);
			final byte[] data = new byte[laenge];
			this.readFully(nbc, data, laenge);
			this.receive(nbc, data);
			GUILogger.i().indicateNetworkReceiveActivity();
		} catch (final ClosedChannelException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		// System.out.println("weird data");
		return true;
	}

	private void receive(final INonBlockingConnection nbc, final byte[] data) {
		System.out.println("received >> " + data.length);
		//send(nbc, data)
		final Connection<?> c = abstractConnections.get(nbc.getId());
		c.onData(data);
	}

	@Override
	public boolean onDisconnect(final INonBlockingConnection nbc) throws IOException {
		//		synchronized (connections) {
		final String id = nbc.getId();
		abstractConnections.get(id).setActive(false);
		System.out.println("Closed Connection: " + id);
		GUILogger.i().log(LogType.SERVER, "Closed Connection: " + id);
		abstractConnections.remove(id);
		actualConnections.remove(id);
		//		}
		return true;
	}

	@Override
	public boolean onConnect(final INonBlockingConnection nbc) {
		final String id = nbc.getId();
		System.out.println("New Connection: " + id);
		GUILogger.i().log(LogType.SERVER, "New Connection! ip:" + nbc.getRemoteAddress() + " id:" + id);
		abstractConnections.put(id, new Connection<LengthEncodedTCPSocketHandler<S>>(nbc.getId(), this, serializer));
		actualConnections.put(id, nbc);
		return true;
	}

	@Override
	public void send(final String connectionId, @NotNull final byte[] bytes, final int length) {
		final INonBlockingConnection nbc = actualConnections.get(connectionId);
		if (length < 1) {
			return;
		}
		/*if (length > 65535) {
			throw new IllegalArgumentException("Overflow: Message exceeds 64K! This is an error message from the 80s!");
		}*/
		final byte[] lengthByte = new byte[4];
		BinaryConverter.writeIntToBytes(length, lengthByte);

		try {
			nbc.write(lengthByte);
			nbc.write(bytes,0,length);
			nbc.flush();
			System.out.println("sent >> 4 + "+length + " bytes");
		} catch (final BufferOverflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final Exception e) {
			System.out.println("Client connection "+nbc.getId()+" lost: declaring as disconnected!");
			try {
				this.onDisconnect(nbc);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void send(final String connectionId, final SerializedMessage serMsg) {
		send(connectionId, serMsg.message, serMsg.length);
	}

	@Override
	public void send(final String connectionId, final byte[] bytes) {
		send(connectionId, bytes, bytes.length);
	}

}
