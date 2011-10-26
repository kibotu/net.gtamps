package client;

import net.gtamps.shared.client.ConnectionManager;

public class DebugClient {
	ConnectionManager cm;
	public DebugClient(){
		cm = new ConnectionManager();
	}
	private void connect(String host, int port) {
		cm.connect(host, port);
	}
}
