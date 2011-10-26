package ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.DebugClient;

public class Controller implements ActionListener{
	public static final String CONNECT = "CONNECT";
	public static final String JOIN = "JOIN";
	public static final String LOGIN = "LOGIN";
	public static final String SET_HOST_AND_PORT = "SET_HOST_AND_PORT";
	
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
			
		}
		if(ae.getActionCommand().equals(LOGIN)){
			
		}
	}
}
