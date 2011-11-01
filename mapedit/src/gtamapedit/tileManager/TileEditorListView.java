package gtamapedit.tileManager;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.metal.MetalIconFactory;

public class TileEditorListView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 886447371939158484L;
	private TileManager tileManager;

	private JList jlist;
	Map<Object, ImageIcon> icons = new HashMap<Object, ImageIcon>();
	
	public TileEditorListView(TileManager tm) {
		this.tileManager = tm;
		
		HashMap<String,TileImageHolder> tilelist = this.tileManager.getTileEntities();
		String[] tileimages  = new String[tilelist.size()];
		
		int i = 0;
		Iterator it = tilelist.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			icons.put(((TileImageHolder)pairs.getValue()).getFilename(), new ImageIcon(((TileImageHolder)pairs.getValue()).getTileImage()));
			tileimages[i] = ((TileImageHolder)pairs.getValue()).getFilename();
			i++;
		}
		
		
		
		
		jlist = new JList(tileimages);
		
		// create a cell renderer to add the appropriate icon

		jlist.setCellRenderer(new TileCellRenderer(icons));

	}
	
	public JList getListView(){
		return jlist;
	}
}
