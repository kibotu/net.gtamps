package gtamapedit.view.menu;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;

public class Menu extends JComponent{
	JButton saveButton = new JButton("save");
	JButton openButton = new JButton("open");
	JButton exportButton = new JButton("export to Level");
	JButton previewButton = new JButton("preview");
	public Menu(MapEditorComponent mapEditorComponent){
		setLayout(new GridLayout(1,0));
		
		this.add(openButton);
		openButton.setActionCommand(ControlType.OPEN_MAP_FILE);
		openButton.addActionListener(mapEditorComponent);
		
		this.add(saveButton);
		saveButton.setActionCommand(ControlType.SAVE_MAP_FILE);
		saveButton.addActionListener(mapEditorComponent);
		
		this.add(exportButton);
		exportButton.setActionCommand(ControlType.EXPORT_MAP_FILE);
		exportButton.addActionListener(mapEditorComponent);
		
		this.add(previewButton);
		previewButton.setActionCommand(ControlType.PREVIEW_MAP_FILE);
		previewButton.addActionListener(mapEditorComponent);
	}

}
