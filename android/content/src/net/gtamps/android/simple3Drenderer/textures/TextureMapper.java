package net.gtamps.android.simple3Drenderer.textures;

import java.util.HashMap;

import net.gtamps.shared.Utils.Logger;
import android.graphics.Bitmap;


public class TextureMapper {
	

	private HashMap<String, float[]> textureCoordLookup;
	private Bitmap bitmap;
	private boolean loaded = false;
	
	public TextureMapper(HashMap<String, float[]> textureCoords, Bitmap tileMap) {
		Logger.i(this, "Generated Tile Map "+tileMap.getWidth()+"x"+tileMap.getHeight());
		/*for(Entry<String, float[]> e : textureCoords.entrySet()){
			Logger.i(this, e.getKey());
			for(float f : e.getValue()){
				Logger.i(this, "   "+f);
			}			
		}*/
		this.textureCoordLookup = textureCoords;
		this.bitmap = tileMap;
	}

	public boolean has(String texture) {
		return textureCoordLookup.containsKey(texture);
	}

	public float[] getCoords(String texture) {
			return textureCoordLookup.get(texture);
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
		Logger.i(this, "TextureMapper has been Loaded to Graphics Memory");
	}

	public int getAmount() {
		return textureCoordLookup.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((textureCoordLookup == null) ? 0 : textureCoordLookup.hashCode());
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
		TextureMapper other = (TextureMapper) obj;
		if (textureCoordLookup == null) {
			if (other.textureCoordLookup != null)
				return false;
		} else if (!textureCoordLookup.equals(other.textureCoordLookup))
			return false;
		return true;
	}

}
