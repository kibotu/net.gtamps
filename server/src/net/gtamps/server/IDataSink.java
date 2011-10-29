package net.gtamps.server;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

public interface IDataSink {

	public void sendData(ByteBuffer data) throws ClosedChannelException;
	
}
