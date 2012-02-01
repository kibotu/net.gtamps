package net.gtamps.android.simple3Drenderer.textures;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import net.gtamps.shared.Utils.Logger;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class TileBitmapBuilder {
	private final int TILE_SIZE;
	private LinkedList<String> tileFileNames;
	private HashMap<String, float[]> textureCoords;
	private Bitmap tileMap;
	private String foldername;
	private AssetManager am;
	
	public TileBitmapBuilder(AssetManager am, String foldername, int tileSize){
		this.foldername = foldername;
		this.tileFileNames = new LinkedList<String>();
		this.textureCoords = new HashMap<String, float[]>();
		this.am = am;
		this.TILE_SIZE = tileSize;
	}
	
	public TileBitmapBuilder(AssetManager am, String foldername){
		this(am,foldername,64);
	}
	public TextureMapper generateTextureMapper() {
		int tileMapSize = (int) Math.ceil(Math.sqrt(tileFileNames.size()));
		this.tileMap = Bitmap.createBitmap(tileMapSize*TILE_SIZE, tileMapSize*TILE_SIZE, Config.ARGB_8888);
		int ts = TILE_SIZE;	
		int tileMapResolution = tileMapSize*TILE_SIZE;
		int i = 0;
		for(String tileFileName: tileFileNames){
			Bitmap singleTile;
			try {
				singleTile = BitmapFactory.decodeStream(am.open(foldername+"/"+tileFileName));
				Logger.e(this, "Sucessfully loaded tile: " + tileFileName);
				int currWidth = singleTile.getWidth();
				int currHeight = singleTile.getHeight();
				int[] pixels = new int[currWidth*currHeight];
				singleTile.getPixels(pixels, 0, singleTile.getWidth(), 0, 0, singleTile.getWidth(), singleTile.getHeight());
				tileMap.setPixels(pixels, 0, singleTile.getWidth(), (i%tileMapSize)*ts, (i/tileMapSize)*ts, singleTile.getWidth(), singleTile.getHeight());
				float[] coords = {
				(float)((i%tileMapSize)*ts)/tileMapResolution, (float)((i/tileMapSize)*ts+currHeight)/tileMapResolution, //bottom left
				(float)((i%tileMapSize)*ts+currWidth)/tileMapResolution, (float)((i/tileMapSize)*ts+currHeight)/tileMapResolution, //bottom right
				(float)((i%tileMapSize)*ts)/tileMapResolution, (float)((i/tileMapSize)*ts)/tileMapResolution, //top right
				(float)((i%tileMapSize)*ts+currWidth)/tileMapResolution, (float)((i/tileMapSize)*ts)/tileMapResolution //top left
				};
				textureCoords.put(tileFileName, coords);
			} catch (IOException e) {
				Logger.e(this, "!!! Failed to load tile: " + tileFileName);
//				e.printStackTrace();
			}
			i++;
		}
		return new TextureMapper(textureCoords,tileMap);
	}
	public TextureMapper putInsideTextureMapper() {
		float[] f = {0f,1f,1f,1f,0f,0f,1f,0f};
		HashMap<String, float[]> map = new HashMap<String, float[]>();
		map.put(tileFileNames.get(0), f);
		Bitmap b = null;
		try {
			b = BitmapFactory.decodeStream(am.open(foldername+"/"+tileFileNames.get(0)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new TextureMapper(map,b);
	}

	public void put(String bitmap) {
		this.tileFileNames.add(bitmap);
	}
	public void putIfAbsent(String bitmap) {
		if(!this.tileFileNames.contains(bitmap)) this.tileFileNames.add(bitmap);
	}
}
