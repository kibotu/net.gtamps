package net.gtamps.server.xsocket;

import net.gtamps.server.Connection;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.serializer.communication.ISerializer;
import org.jetbrains.annotations.NotNull;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ConcurrentHashMap;

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
                Thread.currentThread().sleep(20);
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return b;

    }

    private void readFully(final INonBlockingConnection nbc, final byte[] data) throws IOException {
        assert nbc != null;
        assert data != null;
        int need = data.length;
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

            final byte hi = readByte(nbc);
            final byte lo = readByte(nbc);
            read += 2;

            final int laenge = (((hi) & 0xff) << 8) + ((lo) & 0xff);
            //System.out.println("expecting new message: " + laenge );
            final byte[] data = new byte[laenge];
            this.readFully(nbc, data);
            this.receive(nbc, data);
            Logger.i().indicateNetworkReceiveActivity();
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
        System.out.println(">> " + data.length);
        //send(nbc, data)
        final Connection<?> c = abstractConnections.get(nbc.getId());
        c.onData(data);
    }

    @Override
    public boolean onDisconnect(final INonBlockingConnection nbc) throws IOException {
//		synchronized (connections) {
        final String id = nbc.getId();
        System.out.println("Closed Connection: " + id);
        Logger.i().log(LogType.SERVER, "Closed Connection: " + id);
        abstractConnections.remove(id);
        actualConnections.remove(id);
//		}
        return true;
    }

    @Override
    public boolean onConnect(final INonBlockingConnection nbc) {
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
        final String id = nbc.getId();
        System.out.println("New Connection: " + id);
        Logger.i().log(LogType.SERVER, "New Connection! ip:" + nbc.getRemoteAddress() + " id:" + id);
        abstractConnections.put(id, new Connection<LengthEncodedTCPSocketHandler<S>>(nbc.getId(), this, serializer));
        actualConnections.put(id, nbc);
//		}
        return true;
    }

    @Override
    public void send(final String connectionId, @NotNull final byte[] bytes) {
        final INonBlockingConnection nbc = actualConnections.get(connectionId);
        final int length = bytes.length;
        if (length < 1) {
            return;
        }
        if (length > 65535) {
            throw new IllegalArgumentException("bla!");
        }

        final byte high = (byte) (length >> 8);
        final byte low = (byte) (length & 0xFF);

        try {
            //NonBlockingConnection ncv;
            //ncv.write(buffers);
            nbc.write(high);
            nbc.write(low);
            nbc.write(bytes);
            nbc.flush();
            System.out.println(length + " + 2 bytes send");
            // System.out.println(msg);
        } catch (final BufferOverflowException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*private boolean isKnownConnection(INonBlockingConnection nbc) {
         synchronized (connections) {
             return connections.containsKey(nbc.getId());
         }
     }*/

    private String bytesToStr(final byte[] b) {
        String s = "";
        for (int i = 0; i < b.length; i++) {
            s += (int) b[i] + " ";
        }
        return s;
    }
}
