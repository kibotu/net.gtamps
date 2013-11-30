package main;

import ui.controller.Controller;
import ui.view.ConnectionDialog;
import ui.view.MainFrame;
import client.DebugClient;

public class Main {
	public static void main(String[] args){
		DebugClient dc = new DebugClient();		
		Controller ctrl = new Controller(dc);
		MainFrame mf = new MainFrame(ctrl);
		ConnectionDialog cd = new ConnectionDialog(mf, dc);
	}
}
