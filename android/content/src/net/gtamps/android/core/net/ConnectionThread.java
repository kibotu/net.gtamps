package net.gtamps.android.core.net;

import android.util.Log;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;

public class ConnectionThread implements Runnable{

	private MessageHandler messageHandler;
	private ConnectionManager connection;
	private IWorld world ;

	
	public ConnectionThread(IWorld world){
		this.world = world;
	}
	
	@Override
	public void run() {
		connection = ConnectionManager.INSTANCE;
		                
		this.messageHandler = new MessageHandler(connection, world);
		
		while(!connection.isConnected()){
			Log.e("ConnectionThread", "trying to connect...");
			connection.checkConnection();
			try {
				synchronized (this) {
					wait(10);	
				}				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		connection.checkConnection();
		Log.i("ConnectionThread", "Connected");
		connection.start();
		Log.i("ConnectionThread", "Sending session request");
		connection.add(NewMessageFactory.createSessionRequest());
		Log.i("ConnectionThread", "Sending join request");
		connection.add(NewMessageFactory.createJoinRequest());
		while(true){
			if(connection.isEmpty() && connection.currentRevId>0){
				connection.add(NewMessageFactory.createGetUpdateRequest(connection.currentRevId));
			}
			// handle inbox messages
			while (!connection.isEmpty()) {
				NewMessage messagePolled = connection.poll();
				messagePolled.sendables.resetIterator();
				for (NewSendable sendable : messagePolled.sendables) {
					messageHandler.handleMessage(sendable, messagePolled);
				}
			}
			try {
				synchronized (this) {
					wait(20);	
				}				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
