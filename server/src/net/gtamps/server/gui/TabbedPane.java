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

import net.gtamps.GTAMultiplayerServer;

public class TabbedPane extends JTabbedPane implements ChangeListener, ActionListener {
	public HashMap<LogType, TabbedPaneComponent> panes = new HashMap<LogType, TabbedPaneComponent>();
	private final Timer timer;

	public TabbedPane() {
		super();
		addChangeListener(this);
		for (final LogType lmt : LogType.values()) {
			final TabbedPaneComponent tpc = new TabbedPaneComponent(lmt);
			panes.put(lmt, tpc);
			//JScrollPane jsp = new JScrollPane();
			//jsp.add(tpc);
			//this.addTab(lmt.name(),jsp);
			final JScrollPane scrollPane = new JScrollPane(tpc);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			//scrollPane.add(tpc);
			this.addTab(lmt.name(), scrollPane);
		}
		timer = new Timer(1000, this);
		timer.setInitialDelay(3000);
		timer.start();
	}

	@Override
	public void stateChanged(final ChangeEvent e) {
		if (GUILogger.i().wasUpdated()) {
			updateActivePane();
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		updateActivePane();
	}

	private void updateActivePane() {
		//TODO performance issue?
		if (GUILogger.getInstance().wasUpdated()) {
			for (final LogType lt : LogType.values()) {
				if (GUILogger.getInstance().wasUpdated(lt)) {
					final TabbedPaneComponent tpc = panes.get(lt);
					if (tpc != null) {
						synchronized (GUILogger.lock) {
							final LinkedList<String> copy = new LinkedList<String>(GUILogger.getLogs(tpc.getLogType()));
							tpc.updateLog(copy.subList(Math.max(copy.size()-GTAMultiplayerServer.MAX_LOG_ENTRY_DISPLAY,0), copy.size()-1));
						}
						tpc.invalidate();
					}
				}
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
