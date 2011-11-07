package gtamapedit.view;

import gtamapedit.conf.Configuration;
import gtamapedit.file.MapFile;
import gtamapedit.file.TileBitmapBuilder;
import gtamapedit.preview.MapReference;
import gtamapedit.preview.ProcessingPreview;
import gtamapedit.preview.ProcessingPreviewFrame;
import gtamapedit.tileManager.TileEditorListView;
import gtamapedit.tileManager.TileImageHolder;
import gtamapedit.tileManager.TileManager;
import gtamapedit.view.map.MapElement;
import gtamapedit.view.map.MapModel;
import gtamapedit.view.map.MapView;
import gtamapedit.view.menu.Menu;
import gtamapedit.view.propertyPane.TilePropertyPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.gtamps.shared.game.entity.Entity.Type;
import net.gtamps.shared.game.level.EntityPosition;

import processing.core.PApplet;

public class MapEditorComponent extends JComponent implements ChangeListener, ActionListener, PropertyChangeListener {

	TilePropertyPane tprop;
	TileEditorListView telv;
	MapView mapView;
	MapModel mapModel;
	TileManager tileManager;
	Menu menu;
	
	Modus mode = Modus.TILE_MODE;
	
	public enum Modus{
		TILE_MODE, ENTITY_MODE
	}

	public MapEditorComponent(TileManager tileManager, MapModel mm) {

		this.tileManager = tileManager;
		this.mapModel = mm;

		this.setLayout(new BorderLayout());

		menu = new Menu(this);
		this.add(menu,BorderLayout.NORTH);
		
		mapView = new MapView(mm, this);
		mapView.addPropertyChangeListener(this);
		JScrollPane mapScroll = new JScrollPane(mapView);
		this.add(mapScroll, BorderLayout.CENTER);

		telv = new TileEditorListView(tileManager);
		JScrollPane tileScroll = new JScrollPane(telv.getListView());

		this.add(tileScroll, BorderLayout.WEST);

		tprop = new TilePropertyPane(this);
		tprop.setMode(mode);
		this.add(tprop, BorderLayout.SOUTH);
		//UGLY:
		mapModel.setCurrentEntityType(Type.values()[0]);
		setEditorMode(mode, null);

	}

	public Modus getMode() {
		return mode;
	}
	
	public void stateChanged(ChangeEvent ce) {
		if (ce.getSource().equals(tprop.getFloorSlider())) {
			boolean changedMap = false;
			for (MapElement me : mapModel.getSelectedElements()) {
				me.setFloors(tprop.getFloorSlider().getValue());
				changedMap = true;
				// me.setTextureTop(textureTop)
			}
			if (changedMap) {
				mapView.repaint();
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		boolean changedMap = false;
		if (	ae.getActionCommand().equals(ControlType.TOPTEXTURE) ||
				ae.getActionCommand().equals(ControlType.EASTTEXTURE) || 
				ae.getActionCommand().equals(ControlType.SOUTHTEXTURE) ||
				ae.getActionCommand().equals(ControlType.NORTHTEXTURE) ||
				ae.getActionCommand().equals(ControlType.WESTTEXTURE)
				) {
			if (telv.getListView().getSelectedIndex() != -1) {
				String selectedTile = telv.getListView().getModel().getElementAt(telv.getListView().getSelectedIndex())
						.toString();
				TileImageHolder tileImage = this.tileManager.getTileEntities().get(selectedTile);

				for (MapElement me : mapModel.getSelectedElements()) {
					if(ae.getActionCommand().equals(ControlType.TOPTEXTURE)) me.setTextureTop(tileImage);
					if(ae.getActionCommand().equals(ControlType.EASTTEXTURE)) me.setTextureEast(tileImage);
					if(ae.getActionCommand().equals(ControlType.SOUTHTEXTURE)) me.setTextureSouth(tileImage);
					if(ae.getActionCommand().equals(ControlType.NORTHTEXTURE)) me.setTextureNorth(tileImage);
					if(ae.getActionCommand().equals(ControlType.WESTTEXTURE)) me.setTextureWest(tileImage);
					
					changedMap = true;
					// me.setTextureTop(textureTop)
				}

			}
		}

		if (ae.getActionCommand().equals(ControlType.TEXTURE_ROTATION_0)) {
			for (MapElement me : mapModel.getSelectedElements()) {
				me.setRotation(0);
				changedMap = true;
			}
		}
		if (ae.getActionCommand().equals(ControlType.TEXTURE_ROTATION_90)) {
			for (MapElement me : mapModel.getSelectedElements()) {
				me.setRotation(90);
				changedMap = true;
			}
		}
		if (ae.getActionCommand().equals(ControlType.TEXTURE_ROTATION_180)) {
			for (MapElement me : mapModel.getSelectedElements()) {
				me.setRotation(180);
				changedMap = true;
			}
		}
		if (ae.getActionCommand().equals(ControlType.TEXTURE_ROTATION_270)) {
			for (MapElement me : mapModel.getSelectedElements()) {
				me.setRotation(270);
				changedMap = true;
			}
		}

		if (ae.getActionCommand().equals(ControlType.OPEN_MAP_FILE)) {
			MapFile mf = new MapFile(Configuration.getSavePath());
			mapModel.setMap(mf.getMap(), mf.getEntityPositions());
			mapView.reloadMap();
			changedMap = true;
		}
		if (ae.getActionCommand().equals(ControlType.SAVE_MAP_FILE)) {
			MapFile mapFile = new MapFile(mapModel.getMap(),mapModel.getEntityList());
			mapFile.saveMap(Configuration.getSavePath());
		
		}
		if (ae.getActionCommand().equals(ControlType.EXPORT_MAP_FILE)) {
			MapFile mapFile = new MapFile(mapModel.getMap(),mapModel.getEntityList());
			String report_string = mapFile.exportMap(Configuration.getSavePath()+".lvl");
			JDialog report = new JDialog();
			report.setSize(300, 300);
			report.setLocationRelativeTo(this);
			report.add(new JTextArea(report_string));
			report.setVisible(true);
		}
		if (ae.getActionCommand().equals(ControlType.PREVIEW_MAP_FILE)) {
			MapFile mapFile = new MapFile(mapModel.getMap(),mapModel.getEntityList());
			MapReference.setMapFile(mapFile);
			
			ProcessingPreviewFrame preview = new ProcessingPreviewFrame();  
			
		}
		
		if(ae.getActionCommand().equals(ControlType.SWITCH_MODE)){
			JButton b = (JButton) ae.getSource();
			if(mode.equals(Modus.ENTITY_MODE)){
				changedMap = setEditorMode(Modus.TILE_MODE,b);
			} else {
				changedMap = setEditorMode(Modus.ENTITY_MODE,b);
			}
			
		}
		if(ae.getActionCommand().equals(ControlType.ENTITY_TYPE_CHANGE)){
			JComboBox<String> cb = (JComboBox<String>) ae.getSource();
			String s = (String) cb.getSelectedItem();
			for(Type t : Type.values()){
				if(t.name().equals(s)){
					mapModel.setCurrentEntityType(t);
				}
			}
		}
		if (ae.getActionCommand().equals(ControlType.ENTITY_DELETE_SELECTION)) {
			mapModel.deleteSelectedEntity();
			changedMap = true;
		}
		
		if (changedMap) {
			mapView.repaint();
		}
	}

	private boolean setEditorMode(Modus newmode, JButton b){
		if(newmode==Modus.ENTITY_MODE){
			this.mode = Modus.ENTITY_MODE;
			if(b!=null){
				b.setText("Switch to tile mode");
			}
			tprop.setMode(Modus.ENTITY_MODE);
			return true;
		} else if(newmode == Modus.TILE_MODE){
			this.mode = Modus.TILE_MODE;
			if(b!=null){
				b.setText("Switch to entity mode");
			}
			tprop.setMode(Modus.TILE_MODE);
			return true;
		}
		return false;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean changedMap = false;
		if (evt.getPropertyName().equals(ControlType.NEW_SELECTION)) {
			if (mapModel.getCommonFloors() == -1) {
				tprop.getFloorSlider().setValue(0);
			} else {
				tprop.getFloorSlider().setValue(mapModel.getCommonFloors());
			}
			// TODO
			if (mapModel.getCommonRotation() == -1) {
				
			} else {
				tprop.getTextureRotation().setRotation(mapModel.getCommonRotation());
			}
			if (mapModel.getCommonTopTexture() == null) {
				tprop.getTopTextureSelector().setTexture(null);
			} else {
				tprop.getTopTextureSelector().setTexture(mapModel.getCommonTopTexture());
			}
			if (mapModel.getCommonEastTexture() == null) {
				tprop.getEastTextureSelector().setTexture(null);
			} else {
				tprop.getEastTextureSelector().setTexture(mapModel.getCommonEastTexture());
			}
			if (mapModel.getCommonWestTexture() == null) {
				tprop.getWestTextureSelector().setTexture(null);
			} else {
				tprop.getWestTextureSelector().setTexture(mapModel.getCommonWestTexture());
			}
			if (mapModel.getCommonSouthTexture() == null) {
				tprop.getSouthTextureSelector().setTexture(null);
			} else {
				tprop.getSouthTextureSelector().setTexture(mapModel.getCommonSouthTexture());
			}
			if (mapModel.getCommonNorthTexture() == null) {
				tprop.getNorthTextureSelector().setTexture(null);
			} else {
				tprop.getNorthTextureSelector().setTexture(mapModel.getCommonNorthTexture());
			}
		}
		if (evt.getPropertyName().equals(ControlType.ENTITY_CREATE_SELECT)) {
			System.out.println("entity");
			Point pos = (Point)evt.getNewValue();
			EntityPosition ep = mapModel.getEntityAtPosition(pos);
			if(ep==null){
				System.out.println("create");
				mapModel.createEntityAtPosition(pos, mapModel.getCurrentEntityType());
				changedMap = true;
			} else {
				System.out.println("select");
				mapModel.changeSelection(ep);
				changedMap = true;
			}
		}
		
		if (changedMap) {
			mapView.repaint();
		}
	}

}
