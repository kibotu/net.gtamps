package net.gtamps.server;

public interface IDataSource {
	
	public void registerDataReceiver(IDataReceiver dataReceiver);
	public void removeDataReceiver(IDataReceiver dataReceiver);

}
