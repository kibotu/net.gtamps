package ui.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ui.controller.Controller;

public class MainComponent extends JPanel{
	
	private static final long serialVersionUID = -4201546731148941377L;

	public MainComponent(Controller ctrl){
		super();
		setLayout(new BorderLayout());
		this.add(new StatusBar(ctrl), BorderLayout.SOUTH);
		this.add(new MenuBar(ctrl), BorderLayout.NORTH);
		this.add(new ButtonPanel(ctrl), BorderLayout.CENTER);
	}
}
