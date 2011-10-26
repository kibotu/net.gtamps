package ui.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import ui.controller.Controller;

public class ButtonPanel extends JPanel{
	private static final long serialVersionUID = 1131347123669522075L;
	
	JButton buttonPollUpdate = new JButton("poll Update");
	
	JButton buttonConnect = new JButton("connect");
	JButton buttonLogin = new JButton("login");
	JButton buttonJoin = new JButton("join");
	JButton buttonUpdate = new JButton("get update");
	JButton buttonSessionRequest= new JButton("session request");
	
	public ButtonPanel(Controller ctrl){
		setLayout(new GridLayout(0, 1));
		
		buttonConnect.addActionListener(ctrl);
		buttonConnect.setActionCommand(Controller.CONNECT);
		add(buttonConnect);
		
		buttonSessionRequest.addActionListener(ctrl);
		buttonSessionRequest.setActionCommand(Controller.SESSION_REQUEST);		
		add(buttonSessionRequest);
		
		buttonLogin.addActionListener(ctrl);
		buttonLogin.setActionCommand(Controller.LOGIN);
		add(buttonLogin);
		
		buttonJoin.addActionListener(ctrl);
		buttonJoin.setActionCommand(Controller.JOIN);		
		add(buttonJoin);
		
		buttonUpdate.addActionListener(ctrl);
		buttonUpdate.setActionCommand(Controller.GET_UPDATE);		
		add(buttonUpdate);
		
		buttonPollUpdate.addActionListener(ctrl);
		buttonPollUpdate.setActionCommand(Controller.POLL);		
		add(buttonPollUpdate);
		
		
	}
}
