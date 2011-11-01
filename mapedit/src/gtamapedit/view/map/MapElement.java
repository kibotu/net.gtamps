package gtamapedit.view.map;

import java.io.Serializable;

import gtamapedit.file.MapFileTileElement;
import gtamapedit.tileManager.TileImageHolder;
import gtamapedit.tileManager.TileManager;

public class MapElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5463584418393052725L;
	int floors = 0;
	private TileImageHolder textureTop;
	private TileImageHolder textureNorth;
	private TileImageHolder textureEast;
	private TileImageHolder textureSouth;
	private TileImageHolder textureWest;
	private boolean selected = false;
	private int rotation = 0;
	
	public MapElement(){
		
	}
	
	
	
	public MapElement(MapFileTileElement m) {
		this.floors = m.getFloors();
		this.rotation = m.getRotation();
		this.textureEast = TileManager.getInstance().getTileByFileName(m.getTextureEast()); 
		this.textureWest = TileManager.getInstance().getTileByFileName(m.getTextureWest());
		this.textureNorth = TileManager.getInstance().getTileByFileName(m.getTextureNorth());
		this.textureSouth = TileManager.getInstance().getTileByFileName(m.getTextureSouth());
		this.textureTop = TileManager.getInstance().getTileByFileName(m.getTextureTop());
	}

	public TileImageHolder getTextureTop() {
		return textureTop;
	}



	public void setTextureTop(TileImageHolder textureTop) {
		this.textureTop = textureTop;
	}



	public TileImageHolder getTextureNorth() {
		return textureNorth;
	}



	public void setTextureNorth(TileImageHolder textureNorth) {
		this.textureNorth = textureNorth;
	}



	public TileImageHolder getTextureEast() {
		return textureEast;
	}



	public void setTextureEast(TileImageHolder textureEast) {
		this.textureEast = textureEast;
	}



	public TileImageHolder getTextureSouth() {
		return textureSouth;
	}



	public void setTextureSouth(TileImageHolder textureSouth) {
		this.textureSouth = textureSouth;
	}



	public TileImageHolder getTextureWest() {
		return textureWest;
	}



	public void setTextureWest(TileImageHolder textureWest) {
		this.textureWest = textureWest;
	}



	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}



	public boolean isSelected() {
		return selected;
	}



	public void setSelected(boolean selected) {
		this.selected = selected;
	}



	public void setRotation(int rotation) {
		this.rotation = rotation;
	}



	public int getRotation() {
		return rotation;
	}

	

	
}
