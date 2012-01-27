package net.gtamps.shared.game.level;

import java.io.Serializable;

public class Tile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8696118686755612819L;
	private String bitmapName;
	private float x;
	private float y;
	private float height;
	private int rotation;

	public Tile(String bitmap, float x, float y, float height, int rotation){
		this.bitmapName = bitmap;
		this.x = x;
		this.y = y;
		this.height = height;
		this.rotation = rotation;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bitmapName == null) ? 0 : bitmapName.hashCode());
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + rotation;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (bitmapName == null) {
			if (other.bitmapName != null)
				return false;
		} else if (!bitmapName.equals(other.bitmapName))
			return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (rotation != other.rotation)
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}



	public String getBitmap() {
		return bitmapName;
	}

	public float getY() {
		return y;
	}
	public float getX() {
		return x;
	}
	
	public int getRotation() {
		return rotation;
	}

	public float getHeight() {
		return height;
	}

	public String toString(){
		return "Tile x:"+x+" y:"+y+" h:"+height+" bitmap:"+bitmapName;
	}
}
