package gtamapedit.view.propertyPane;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;
import gtamapedit.view.MapEditorComponent.Modus;
import gtamapedit.view.propertyPane.components.FloorSlider;
import gtamapedit.view.propertyPane.components.SideTextureSelectors;
import gtamapedit.view.propertyPane.components.TextureRotation;
import gtamapedit.view.propertyPane.components.TextureSelector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

import net.gtamps.shared.game.entity.Entity.Type;

public class TilePropertyPane extends JComponent{
	
	FloorSlider floorSlider;
	TextureSelector topTexture;
	SideTextureSelectors sideTexture;
	TextureRotation textureRotation; 
	MapEditorComponent mec;
	
	JPanel tilePropertyPanel = new JPanel();
	
	JButton entityDelete = new JButton("delete selected entity");
	JComboBox<String> entityTypeList;
		
	JPanel entityPropertyPanel = new JPanel();
	
	JButton modeSwitcher = new JButton("Switch to Entity mode");
	
	public TilePropertyPane(MapEditorComponent mec){
		floorSlider = new FloorSlider(mec);
		topTexture = new TextureSelector("Top Texture",ControlType.TOPTEXTURE,mec);
		sideTexture = new SideTextureSelectors(mec);
		textureRotation = new TextureRotation(mec);
		
		modeSwitcher.setActionCommand(ControlType.SWITCH_MODE);
		modeSwitcher.addActionListener(mec);
			
		floorSlider.addChangeListener(mec);
		this.setLayout(new BorderLayout());
		this.add(modeSwitcher,BorderLayout.WEST);
		
		tilePropertyPanel.setLayout(new FlowLayout());
		tilePropertyPanel.add(floorSlider);
		tilePropertyPanel.add(topTexture);
		tilePropertyPanel.add(sideTexture);
		tilePropertyPanel.add(textureRotation);
		//this.add(tilePropertyPanel);
		
		entityPropertyPanel.setLayout(new FlowLayout());
		entityDelete.addActionListener(mec);
		entityDelete.setActionCommand(ControlType.ENTITY_DELETE_SELECTION);
		entityPropertyPanel.add(entityDelete);
		String[] entityTypes = new String[Type.values().length];
		int i = 0;
		for(Type t : Type.values()){
			entityTypes[i] = t.name();
			i++;
		}
		entityTypeList = new JComboBox<String>(entityTypes);
		entityTypeList.addActionListener(mec);
		entityTypeList.setActionCommand(ControlType.ENTITY_TYPE_CHANGE);
		entityPropertyPanel.add(entityTypeList);
		//this.add(entityPropertyPanel);
	}
	
	public JSlider getFloorSlider() {
		return floorSlider;
	}
	public TextureSelector getTopTextureSelector(){
		return topTexture;
	}
	public TextureSelector getNorthTextureSelector(){
		return sideTexture.getNorthTexture();
	}
	public TextureSelector getSouthTextureSelector(){
		return sideTexture.getSouthTexture();
	}
	public TextureSelector getEastTextureSelector(){
		return sideTexture.getEastTexture();
	}
	public TextureSelector getWestTextureSelector(){
		return sideTexture.getWestTexture();
	}
	public TextureRotation getTextureRotation() {
		return textureRotation;
	}

	public void setMode(Modus mode) {
		if(mode==Modus.ENTITY_MODE){
			this.remove(tilePropertyPanel);
			this.add(entityPropertyPanel,BorderLayout.CENTER);
		}
		if(mode==Modus.TILE_MODE){
			this.add(tilePropertyPanel,BorderLayout.CENTER);
			this.remove(entityPropertyPanel);
		}
	}

}
