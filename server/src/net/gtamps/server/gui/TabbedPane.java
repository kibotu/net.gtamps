package net.gtamps.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPane extends JTabbedPane implements ChangeListener, ActionListener{
	public HashMap<LogType, TabbedPaneComponent> panes = new HashMap<LogType, TabbedPaneComponent>();
	private Timer timer; 
	public TabbedPane(){
		super();
		this.addChangeListener(this);
		for(LogType lmt : LogType.values()){
			TabbedPaneComponent tpc = new TabbedPaneComponent(lmt);
			panes.put(lmt, tpc);
			//JScrollPane jsp = new JScrollPane();
			//jsp.add(tpc);
			//this.addTab(lmt.name(),jsp);
			JScrollPane scrollPane = new JScrollPane(tpc);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			//scrollPane.add(tpc);
			this.addTab(lmt.name(),scrollPane);
		}
		timer = new Timer(1000, this);
		timer.setInitialDelay(3000);
		timer.start(); 
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(Logger.i().wasUpdated()){
			updateActivePane();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateActivePane();	
	}
	
	private void updateActivePane(){
		//TODO performance issue?
		for(LogType lt : LogType.values()){
			TabbedPaneComponent tpc = this.panes.get(lt);
			if(tpc!=null){
				synchronized (Logger.lock) {
					LinkedList<String> copy = new LinkedList<String>(Logger.getLogs(tpc.getLogType()));
					tpc.updateLog(copy);
				}
				tpc.invalidate();
			}
		}
	}
	
	/*public Graphics repaint(Graphics g){
		super.repaint();
		for(LogType t : panes.keySet()){
			panes.get(t).updateLog(Logger.getLogs(t));
			System.out.println(Logger.getLogs(t));
		}
		
		return g;
	}*/
}
