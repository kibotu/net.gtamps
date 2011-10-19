package android.normalizer.view;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import android.normalizer.controller.Controller;

public class MenuBar extends JMenuBar {

	public MenuBar(ActionListener listener) {
		super();
		
		addFileMenu(listener);
	}
	
	public void addFileMenu(ActionListener listener) {
		final JMenu menu= new JMenu("File");
		for(final Controller.FileCommand command: Controller.FileCommand.values()) {
			final JMenuItem item = new JMenuItem(command.name().replaceAll("_", " "));
			item.setActionCommand(command.name());
			item.addActionListener(listener);
			menu.add(item);
		}
		add(menu);
	}
}
