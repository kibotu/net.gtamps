package ui.view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import ui.controller.Controller;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = -577136610625197794L;

	public MainFrame(Controller ctrl){
		super("Debug Client");
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(new MainComponent(ctrl));
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
