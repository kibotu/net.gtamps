package net.gtamps.server.gui;

import java.util.LinkedList;

import javax.swing.JTextArea;

public class TabbedPaneComponent extends JTextArea{
	private LogType logType;
	
	public TabbedPaneComponent(LogType lmt){
		this.logType = lmt;
		this.setEditable(false);
	}
	
	public LogType getLogType(){
		return this.logType;
	}
	
	public void updateLog(LinkedList<String> ls){
		StringBuilder sb = new StringBuilder();
		for(String s : ls){
			sb.append(s+"\r\n");
		}
		this.setText(sb.toString());
//		this.repaint();
		this.invalidate();
	}
}
