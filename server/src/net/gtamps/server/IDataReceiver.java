package net.gtamps.server;

import java.nio.ByteBuffer;

public interface IDataReceiver {

	public void receiveData(ByteBuffer data, int length);
	
}
