package net.gtamps.android.core.net;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtamps.android.core.net.IMessageManager;
import net.gtamps.android.core.net.IStream;
import net.gtamps.android.core.net.RemoteInputDispatcher;
import net.gtamps.android.core.net.RemoteOutputDispatcher;
import net.gtamps.android.core.net.TcpStream;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.BinaryObjectSerializer;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.helper.SerializedMessage;

import org.jetbrains.annotations.NotNull;

public enum ConnectionManager implements IMessageManager {

	INSTANCE;

	private final ConcurrentLinkedQueue<NewMessage> inbox;
	private final ConcurrentLinkedQueue<NewMessage> outbox;
	private IStream stream;
	private RemoteInputDispatcher remoteInputDispatcher;
	private RemoteOutputDispatcher remoteOutputDispatcher;
	public volatile String currentSessionId;
	public volatile long currentRevId;
	private final int currentSocketTimeOut = Config.MAX_SOCKET_TIMEOUT;

	private static ISerializer serializer = new BinaryObjectSerializer();

	private ConnectionManager() {
		stream = new TcpStream();
		inbox = new ConcurrentLinkedQueue<NewMessage>();
		outbox = new ConcurrentLinkedQueue<NewMessage>();
		currentSessionId = "0";
		currentRevId = 0;
	}
	
	@Override
	public boolean isEmpty() {
		return inbox.isEmpty();
	}

	@Override
	public NewMessage poll() {
		final NewMessage message = inbox.poll();
//		currentSessionId = message.getSessionId();
		//remove string allocations
//		if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
//			Logger.i(this, "inbox poll: " + message.toString());
//		}
		return message;
	}

	@Override
	public boolean add(@NotNull final NewMessage message) {
		message.setSessionId(currentSessionId);
		//remove string allocations
//		if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
//			Logger.i(this, "outbox add: " + message.toString());
//		}
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
		remoteInputDispatcher.stop();
		remoteOutputDispatcher.stop();
		return stream.disconnect();
	}

	public boolean isConnected() {
		return stream.isConnected();
	}

	public void start() {
		if (!isConnected()) {
			return;
		}
		remoteInputDispatcher = new RemoteInputDispatcher(stream, inbox);
		remoteOutputDispatcher = new RemoteOutputDispatcher(stream, outbox);
		remoteInputDispatcher.start();
		remoteOutputDispatcher.start();
	}

	public void stop() {
		if (!isConnected()) {
			return;
		}
		remoteInputDispatcher.stop();
		remoteOutputDispatcher.stop();
	}

	public SerializedMessage serialize(@NotNull final NewMessage message) {
		message.setSessionId(currentSessionId);
		return serializer.serializeAndPackNewMessage(message);
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
	
}

