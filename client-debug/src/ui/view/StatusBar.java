package ui.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.controller.Controller;

public class StatusBar extends JPanel {
	JTextField statusText = new JTextField("Statusbar... [implement me!]");
	public StatusBar(Controller ctrl){
		setPreferredSize(new Dimension(800,20));
		this.add(statusText);
	}
}
