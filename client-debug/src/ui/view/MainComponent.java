package ui.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ui.controller.Controller;

public class MainComponent extends JPanel{
	
	private static final long serialVersionUID = -4201546731148941377L;

	JPanel mainPanel =  new JPanel();
	
	public MainComponent(Controller ctrl){
		super();
		
		mainPanel.setLayout(new BorderLayout());
		
		setLayout(new BorderLayout());
		this.add(new StatusBar(ctrl), BorderLayout.SOUTH);
		this.add(new MenuBar(ctrl), BorderLayout.NORTH);
		
		mainPanel.add(new ButtonPanel(ctrl),BorderLayout.WEST);
		this.add(mainPanel, BorderLayout.CENTER);
	}
}
