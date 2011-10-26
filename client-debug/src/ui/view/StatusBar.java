package ui.view;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.controller.Controller;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = -6366186729995089015L;
	JLabel statusText = new JLabel("Statusbar... [implement me!]");
	public StatusBar(Controller ctrl){
		setPreferredSize(new Dimension(800,20));
		this.add(statusText);
	}
}
