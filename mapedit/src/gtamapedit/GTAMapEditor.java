package gtamapedit;

import gtamapedit.conf.Configuration;
import gtamapedit.conf.ConfigurationDialog;
import gtamapedit.tileManager.TileManager;
import gtamapedit.view.MapEditorComponent;
import gtamapedit.view.map.MapModel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GTAMapEditor {
		
	public static void main(String[] args) {
		JFrame frame = new JFrame("GTAMPS Map Editor");
		
		ConfigurationDialog cd = new  ConfigurationDialog(frame, true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,600);
		
		frame.setLayout(new BorderLayout());
		
		TileManager tileManager = TileManager.getInstance();
		MapModel mapModel = new MapModel(Configuration.getMapSizeX(), Configuration.getMapSizeY());
		
		frame.add(new MapEditorComponent(tileManager, mapModel), BorderLayout.CENTER);
		
		frame.setVisible(true);

		
	}

}
