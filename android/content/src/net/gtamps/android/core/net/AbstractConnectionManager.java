package net.gtamps.android.core.net;

import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.helper.SerializedMessage;

public abstract class AbstractConnectionManager {
	private static AbstractConnectionManager INSTANCE;
	private static Class<? extends AbstractConnectionManager> implementingClass = null;
	
	public volatile Long currentRevId;
	public volatile String currentSessionId;

	public abstract NewMessage poll();

	public abstract boolean isEmpty();
	public abstract boolean add(NewMessage createGetUpdateRequest);
	
	public static void setImplementingClass(Class<? extends AbstractConnectionManager> cls){
		implementingClass = cls;		
	}
	
	public static AbstractConnectionManager getInstance(){
		if(INSTANCE==null){
			if(implementingClass==null){
				throw new IllegalStateException("You have to call setImplementingClass(Class<AbstractConnectionManager>) before use!");
			}
			try {
				INSTANCE = implementingClass.newInstance();
				INSTANCE.currentRevId = 0L;
				INSTANCE.currentSessionId = "";
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		return INSTANCE;
	}

	abstract public NewMessage deserialize(byte[] response);

	abstract public SerializedMessage serialize(NewMessage poll);

	abstract public boolean isConnected();

	abstract public void checkConnection();

	abstract public void start();
	
}
