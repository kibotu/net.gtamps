package gtamapedit.file;

import gtamapedit.view.map.MapElement;

import java.io.Serializable;

public class MapFileTileElement implements Serializable {
	int floors = 0;
	private String textureTop;
	private String textureNorth;
	private String textureEast;
	private String textureSouth;
	private String textureWest;
	private int rotation = 0;

	public MapFileTileElement(MapElement map) {
		this.floors = map.getFloors();

		if (map.getTextureTop() != null) {
			this.textureTop = map.getTextureTop().getFilename();
		} else {
			this.textureTop = "";
		}

		if (map.getTextureEast() != null) {
			this.textureEast = map.getTextureEast().getFilename();
		} else {
			this.textureEast = "";
		}
		if (map.getTextureNorth() != null) {
			this.textureNorth = map.getTextureNorth().getFilename();
		} else {
			this.textureNorth = "";
		}
		if (map.getTextureSouth() != null) {
			this.textureSouth = map.getTextureSouth().getFilename();
		} else {
			this.textureSouth = "";
		}
		if (map.getTextureWest() != null) {
			this.textureWest = map.getTextureWest().getFilename();
		} else {
			this.textureWest = "";
		}

		this.rotation = map.getRotation();
	}

	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public String getTextureTop() {
		return textureTop;
	}

	public void setTextureTop(String textureTop) {
		this.textureTop = textureTop;
	}

	public String getTextureNorth() {
		return textureNorth;
	}

	public void setTextureNorth(String textureNorth) {
		this.textureNorth = textureNorth;
	}

	public String getTextureEast() {
		return textureEast;
	}

	public void setTextureEast(String textureEast) {
		this.textureEast = textureEast;
	}

	public String getTextureSouth() {
		return textureSouth;
	}

	public void setTextureSouth(String textureSouth) {
		this.textureSouth = textureSouth;
	}

	public String getTextureWest() {
		return textureWest;
	}

	public void setTextureWest(String textureWest) {
		this.textureWest = textureWest;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
}
