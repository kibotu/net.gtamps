package ui.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import ui.controller.Controller;

public class ButtonPanel extends JPanel{
	private static final long serialVersionUID = 1131347123669522075L;
	
	JButton buttonConnect = new JButton("connect");
	JButton buttonLogin = new JButton("login");
	JButton buttonJoin = new JButton("join");
	public ButtonPanel(Controller ctrl){
		setLayout(new GridLayout(3, 3));
		
		buttonConnect.addActionListener(ctrl);
		buttonConnect.setActionCommand(Controller.CONNECT);
		add(buttonConnect);
		
		buttonLogin.addActionListener(ctrl);
		buttonLogin.setActionCommand(Controller.LOGIN);
		add(buttonLogin);
		
		buttonJoin.addActionListener(ctrl);
		buttonJoin.setActionCommand(Controller.JOIN);		
		add(buttonJoin);
	}
}
