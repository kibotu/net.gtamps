package net.gtamps.shared.serializer;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.communication.NewMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteInputDispatcher implements Runnable {

	private static final String TAG = RemoteInputDispatcher.class.getSimpleName();

	private volatile boolean isRunning;
	@Nullable
	private final IStream inputStream;
	private final ConcurrentLinkedQueue<NewMessage> inbox;

	public RemoteInputDispatcher(@Nullable final IStream stream, @NotNull final ConcurrentLinkedQueue<NewMessage> inbox) {
		isRunning = false;
		this.inputStream = stream;
		this.inbox = inbox;
	}

	public void start() {
		new Thread(this).start();
	}

	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		Logger.i(this, "Starts socket-listening loop.");
		isRunning = true;
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		byte[] response;
		int length;

		while (isRunning) {
				//  latency
				try {
					Thread.sleep(Config.SOCKET_INBOX_LATENCY);
				} catch (final InterruptedException e) {
					Logger.e(this, e.getMessage());
				}

				response = inputStream.receive();
				if(response!=null){
					final NewMessage message = ConnectionManager.INSTANCE.deserialize(response);
					Logger.d(this, message);
					if (message != null) {
						inbox.add(message);
					}
				}

		}
		Logger.i(this, "Stops socket-listening loop.");
	}
}