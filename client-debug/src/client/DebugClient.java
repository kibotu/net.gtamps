package client;

import net.gtamps.shared.client.ConnectionManager;

public class DebugClient {
	private ConnectionManager cm;
	private String host;
	private int port;
	
	public DebugClient(){
		cm = new ConnectionManager();
	}
	public void connect() {
		cm.connect(host, port);
	}
	public void setHostAndPort(String host, int port){
		this.host = host;
		this.port = port;
		System.out.println("set host and port "+host+":"+port);
	}
}
