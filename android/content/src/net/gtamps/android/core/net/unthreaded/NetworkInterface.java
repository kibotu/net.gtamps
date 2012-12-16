package net.gtamps.android.core.net.unthreaded;

import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtamps.android.core.net.AbstractConnectionManager;
import net.gtamps.android.core.net.IStream;
import net.gtamps.android.core.net.TcpStream;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.BinaryObjectSerializer;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.helper.SerializedMessage;

import org.jetbrains.annotations.NotNull;

public class NetworkInterface extends AbstractConnectionManager {

	private final ConcurrentLinkedQueue<NewMessage> inbox;
	private final ConcurrentLinkedQueue<NewMessage> outbox;
	private IStream stream;
	private final int currentSocketTimeOut = Config.MAX_SOCKET_TIMEOUT;

	private static ISerializer serializer = new BinaryObjectSerializer();
	
	public NetworkInterface() {
		stream = new TcpStream();
		inbox = new ConcurrentLinkedQueue<NewMessage>();
		outbox = new ConcurrentLinkedQueue<NewMessage>();
	}
	
	@Override
	public boolean isEmpty() {
		return inbox.isEmpty();
	}

	@Override
	public NewMessage poll() {
		final NewMessage message = inbox.poll();
		return message;
	}

	@Override
	public boolean add(@NotNull final NewMessage message) {
		message.setSessionId(currentSessionId);
		if(outbox.size()<Config.MAX_MESSAGES_OUTBOX){
			return outbox.add(message);
		} else {
			return false;
		}
	}

	public boolean connect() {
		return connect(Config.SERVER_DEFAULT_HOST_ADDRESS, Config.SERVER_DEFAULT_PORT);
	}

	public boolean connect(final String host, final int port) {
		if (isConnected()) {
			return false;
		}
		return stream.connect(host, port);
	}

	public boolean disconnect() {
		if (!isConnected()) {
			return false;
		}
		return stream.disconnect();
	}

	public boolean isConnected() {
		return stream.isConnected();
	}

	public void start() {
		if (!isConnected()) {
			return;
		}
	}

	public void stop() {
		if (!isConnected()) {
			return;
		}
	}

	SerializedMessage sm = null;
	public SerializedMessage serialize(@NotNull final NewMessage message) {
		message.setSessionId(currentSessionId);
		sm = serializer.serializeAndPackNewMessage(message);
		message.sendables.recycle();
		message.recycle();
		return sm; 
	}

	public NewMessage deserialize(@NotNull final byte[] message) {
		return serializer.deserializeNewMessage(message);
	}

	public void checkConnection() {
		if (!isConnected()) {
			int i = 0;
			while (!connect(Config.IPS.get(i), Config.SERVER_DEFAULT_PORT)) {
				Logger.i(this, "Connecting to " + Config.IPS.get(i) + ":" + Config.SERVER_DEFAULT_PORT);
				if (i < Config.IPS.size() - 1) {
					i++;
				} else {
					return;
				}
				Logger.e(this, "Server unavailable. Trying to reconnect");
			}
		}
	}

	public void update() {
		listen();
		talk();
	}
	
	//preallocate
	private byte[] response;
	public void listen() {
		response = stream.receive();
		if (response != null) {
			if (inbox.size() < Config.MAX_MESSAGES_INBOX) {
				final NewMessage message = AbstractConnectionManager.getInstance().deserialize(response);
				// Logger.d(this, message);
				if (message != null) {
					inbox.add(message);
				}
			}
		}

	}
	
	//preallocate	
	private SerializedMessage serializedMessage;
	public void talk() {
		if (outbox.isEmpty()) {
			return;
		}
		serializedMessage = AbstractConnectionManager.getInstance().serialize(outbox.poll());
		try {
			stream.send(serializedMessage.message,serializedMessage.length);
		} catch (SocketException e) {
//			throw new StopTheGameException("Connection lost.");
		}
		
	}
}
	

