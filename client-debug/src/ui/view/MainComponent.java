package ui.view;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import ui.controller.Controller;

public class MainComponent extends JPanel{
	public MainComponent(Controller ctrl){
		super();
		setLayout(new BorderLayout());
		this.add(new StatusBar(ctrl), BorderLayout.SOUTH);
		this.add(new MenuBar(ctrl), BorderLayout.NORTH);
	}
}
