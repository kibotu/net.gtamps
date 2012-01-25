package net.gtamps.server.gui;

import java.util.List;

import javax.swing.JTextArea;

public class TabbedPaneComponent extends JTextArea {
	private final LogType logType;

	public TabbedPaneComponent(final LogType lmt) {
		logType = lmt;
		setEditable(false);
	}

	public LogType getLogType() {
		return logType;
	}

	public void updateLog(final List<String> list) {
		final StringBuilder sb = new StringBuilder();
		for (final String s : list) {
			sb.append(s + "\r\n");
		}
		setText(sb.toString());
		//		this.repaint();
		invalidate();
		try {
			Thread.sleep(200);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}
	}
}
