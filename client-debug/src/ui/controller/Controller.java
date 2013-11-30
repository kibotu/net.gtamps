package ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.DebugClient;

public class Controller implements ActionListener{
	public static final String CONNECT = "CONNECT";
	public static final String JOIN = "JOIN";
	public static final String LOGIN = "LOGIN";
	public static final String SET_HOST_AND_PORT = "SET_HOST_AND_PORT";
	public static final String GET_UPDATE = "GET_UPDATE";
	public static final String SESSION_REQUEST = "SESSION_REQUEST";
	public static final String POLL = "POLL";
	
	private DebugClient dc;
	public Controller(DebugClient dc){
		this.dc = dc;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals(CONNECT)){
			dc.connect();
		}
		if(ae.getActionCommand().equals(JOIN)){
			dc.join();
		}
		if(ae.getActionCommand().equals(LOGIN)){
			dc.login();
		}
		if(ae.getActionCommand().equals(GET_UPDATE)){
			dc.getUpdate();
		}
		if(ae.getActionCommand().equals(SESSION_REQUEST)){
			dc.createSession();
		}
		if(ae.getActionCommand().equals(POLL)){
			dc.poll();
		}
		
	}
}
