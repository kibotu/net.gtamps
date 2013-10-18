package net.gtamps.android.gtandroid.core.net.unthreaded;

import android.util.Log;
import net.gtamps.android.gtandroid.core.net.AbstractConnectionManager;
import net.gtamps.android.gtandroid.core.net.IWorld;
import net.gtamps.android.gtandroid.core.net.MessageHandler;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;

public class ConnectionCenter {

    private static final long NETWORK_WAIT_FOR_ACTIVITY = 10;
    private MessageHandler messageHandler;
    private NetworkInterface connection;
    private IWorld world;

    public ConnectionCenter(IWorld world) {
        this.world = world;
        this.connection = (NetworkInterface) AbstractConnectionManager.getInstance();
        this.messageHandler = new MessageHandler(connection, world);
    }

    public void establishConnection() {
        while (!this.connection.isConnected()) {
            Log.e("ConnectionThread", "trying to connect...");
            this.connection.checkConnection();
            try {
                synchronized (this) {
                    wait(10);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.connection.checkConnection();
        Log.i("ConnectionThread", "Connected");
        this.connection.start();
        Log.i("ConnectionThread", "Sending session request");
        this.connection.add(NewMessageFactory.createSessionRequest());
    }

    public void handleConnection() {
        connection.update();

        if (this.connection.isEmpty() && this.connection.currentRevId > 0) {
            this.connection.add(NewMessageFactory.createGetUpdateRequest(this.connection.currentRevId));
        }
        // handle inbox messages
        while (!this.connection.isEmpty()) {
            NewMessage messagePolled = this.connection.poll();
            messagePolled.sendables.resetIterator();
            for (NewSendable sendable : messagePolled.sendables) {
                messageHandler.handleMessage(sendable, messagePolled);
            }
        }
    }
}
