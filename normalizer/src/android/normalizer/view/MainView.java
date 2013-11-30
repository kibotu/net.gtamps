package android.normalizer.view;

import java.awt.BorderLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import android.normalizer.Config;
import android.normalizer.controller.Controller;


public class MainView extends JPanel {

	public MainView(Controller context) {
		super();
		
		setLayout(new BorderLayout());
		
		add(new MenuBar(context), BorderLayout.NORTH);
	}
}
