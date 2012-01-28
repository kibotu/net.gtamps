package net.gtamps.android.core.net;


import net.gtamps.android.core.net.ConnectionManager;
import net.gtamps.android.core.net.IStream;
import net.gtamps.android.core.net.RemoteOutputDispatcher;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.SendableProvider;
import net.gtamps.shared.serializer.communication.SendableProviderSingleton;
import net.gtamps.shared.serializer.helper.SerializedMessage;

import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteOutputDispatcher extends Observable implements Runnable {

	private static final String TAG = RemoteOutputDispatcher.class.getSimpleName();

	private volatile boolean isRunning;
	private final byte[] responseBytes;
	private byte[] response;
	private int length;
	private final IStream tcpStream;
	private final ConcurrentLinkedQueue<NewMessage> outbox;
	private SerializedMessage serializedMessage;

	public RemoteOutputDispatcher(final IStream tcpStream, final ConcurrentLinkedQueue<NewMessage> outbox) {
		isRunning = false;
		responseBytes = new byte[2];
		this.tcpStream = tcpStream;
		this.outbox = outbox;
	}

	public void start() {
		new Thread(this).start();
	}

	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		Logger.i(this, "Starts socket- writing loop.");
		isRunning = true;
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while (isRunning) {
			try {
				Thread.currentThread().sleep(Config.SOCKET_OUTBOX_LATENCY);
			} catch (final InterruptedException e) {
				Logger.i(this, e.getMessage());
			}
			if (outbox.isEmpty()) {
				continue;
			}
			serializedMessage = ConnectionManager.INSTANCE.serialize(outbox.poll());
			tcpStream.send(serializedMessage.message,serializedMessage.length);
			
		}
		Logger.i(this, "Stops socket-listening loop.");
	}

}
