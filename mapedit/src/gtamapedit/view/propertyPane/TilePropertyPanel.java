package gtamapedit.view.propertyPane;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;
import gtamapedit.view.propertyPane.tilePanel.FloorSlider;
import gtamapedit.view.propertyPane.tilePanel.SideTextureSelectors;
import gtamapedit.view.propertyPane.tilePanel.TextureRotation;
import gtamapedit.view.propertyPane.tilePanel.TextureSelector;

public class TilePropertyPanel extends JPanel{
	FloorSlider floorSlider;
	TextureSelector topTexture;
	SideTextureSelectors sideTexture;
	TextureRotation textureRotation; 
	
	public TilePropertyPanel(MapEditorComponent mec) {
		floorSlider = new FloorSlider(mec);
		topTexture = new TextureSelector("Top Texture",ControlType.TOPTEXTURE,mec);
		sideTexture = new SideTextureSelectors(mec);
		textureRotation = new TextureRotation(mec);
		
		floorSlider.addChangeListener(mec);
		
		this.setLayout(new FlowLayout());
		this.add(floorSlider);
		this.add(topTexture);
		this.add(sideTexture);
		this.add(textureRotation);
	}
	public TextureSelector getTopTexture() {
		return topTexture;
	}
	public SideTextureSelectors getSideTexture() {
		return sideTexture;
	}
	public FloorSlider getFloorSlider() {
		return floorSlider;
	}
	public TextureRotation getTextureRotation() {
		return textureRotation; 
	}
}
