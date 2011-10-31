package gtamapedit.tileManager;

import gtamapedit.conf.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class TileManager {
	private HashMap<String,TileImageHolder> tileImages = new HashMap<String,TileImageHolder>();

	private static TileManager instance = null; 
	
	public static TileManager getInstance(){
		if(instance==null){
			instance = new TileManager(Configuration.getTileImagePath());
		}
		return instance;
	}
	
	private TileManager(String path) {
		loadTilesFromFolder(path);
	}

	public void loadTilesFromFolder(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles) {
			tileImages.put(f.getName(),new TileImageHolder(f));
		}
	}

	public HashMap<String,TileImageHolder> getTileEntities() {
		return tileImages;
	}
	public TileImageHolder getTileByFileName(String filename){
		if(tileImages.containsKey(filename)){
			return tileImages.get(filename);
		}
		return null;
	}
}
