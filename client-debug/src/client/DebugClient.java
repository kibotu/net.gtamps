package client;

import java.io.IOException;
import java.io.InputStream;

import net.gtamps.android.core.net.ConnectionManager;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.MessageFactory;
import net.gtamps.shared.serializer.communication.Sendable;



public class DebugClient {
	
	long revId = 0;
	
	private ConnectionManager cm;
	private String host;
	private int port;
	
	public DebugClient(){
		cm = new ConnectionManager();
	}
	public void connect() {
		cm.connect(host, port);
		cm.start();
	}
	public void join() {
		cm.add(MessageFactory.createJoinCommand());
	}
	public void getUpdate() {
		cm.add(MessageFactory.createGetUpdateRequest(revId));
	}
	public void setHostAndPort(String host, int port){
		this.host = host;
		this.port = port;
		System.out.println("set host and port "+host+":"+port);
	}
	public void createSession(){
		cm.add(MessageFactory.createSessionRequest());
	}
	public void login() {
		cm.add(MessageFactory.createLoginRequest("user", "password"));
	}
	public void poll(){
		while(!cm.isEmpty()){
			Message m = cm.poll();
			for(Sendable s:m.sendables){
				System.out.println(s.toString());
			}
		}
	}
}
