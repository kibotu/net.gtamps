package ui.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

import main.Configuration;

import client.DebugClient;

import ui.controller.Controller;

public class ConnectionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 4339183368391989900L;
	
	JTextField host;
	JTextField port;
	DebugClient dc;
	public ConnectionDialog(JFrame f, DebugClient dc){
		super(f, true);
		this.dc = dc;
		
		setLayout(new GridLayout(3,0));
		host = new JTextField(Configuration.defaultHost);
		this.add(host);
		port = new JTextField(Configuration.defaultPort+"");
		this.add(port);
		
		JButton okbutton = new JButton("ok");
		okbutton.addActionListener(this);
		okbutton.setActionCommand(Controller.SET_HOST_AND_PORT);
		add(okbutton);
		
		
		setSize(200, 200);
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals(Controller.SET_HOST_AND_PORT)){
			dc.setHostAndPort(this.host.getText(), Integer.parseInt(this.port.getText()));
			this.dispose();
		}
		
	}
}
