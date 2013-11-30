package gtamapedit.conf;

import java.io.File;
import java.util.HashMap;

public class Configuration {
	private static String tileImagePath = new File("").getAbsolutePath()+"/tile_images/";
	private static String savePath = new File("").getAbsolutePath()+"/map1.map";
	private static String levelSavePath = new File("..").getAbsolutePath()+"/assets/map1.map.lvl";
	private static int maxFloors = 10;
	private static int mapSizeX = 20;
	private static int mapSizeY = 20;
	
	private static enum Titles{
		Tile_Image_Path, Project_Map_Save_Path, Level_Save_Path,Max_Floors, Map_Size_X, Map_Size_Y
	}
	
	public static final int tileSize = 64;
	public static final int ENTITY_SIZE = 10;

	public static String getTileImagePath() {
		return tileImagePath;
	}
	public static int getMaxFloors() {
		return maxFloors;
	}
	public static int getMapSizeX() {
		return mapSizeX;
	}
	public static int getMapSizeY() {
		return mapSizeY;
	}
	public static String getSavePath() {
		return savePath;
	}
	public static String getLevelSavePath() {
		return levelSavePath;
	}
	public static HashMap<String, Object> getConfigurations(){
		HashMap<String, Object> configs = new HashMap<String, Object>();
		configs.put(Titles.Tile_Image_Path.toString(), tileImagePath);
		configs.put(Titles.Project_Map_Save_Path.toString(), savePath);
		configs.put(Titles.Max_Floors.toString(), maxFloors);
		configs.put(Titles.Map_Size_X.toString(), mapSizeX);
		configs.put(Titles.Map_Size_Y.toString(), mapSizeY);
		configs.put(Titles.Level_Save_Path.toString(), levelSavePath);
		return configs;
	}
	public static void setConfig(String title, String value) {
		if(title.equals(Titles.Tile_Image_Path.toString())){
			tileImagePath = value;
		}
		if(title.equals(Titles.Project_Map_Save_Path.toString())){
			savePath = value;
		}
		if(title.equals(Titles.Level_Save_Path.toString())){
			levelSavePath = value;
		}
		if(title.equals(Titles.Max_Floors.toString())){
			maxFloors = Integer.parseInt(value);
		}
		if(title.equals(Titles.Map_Size_X.toString())){
			mapSizeX= Integer.parseInt(value);
		}
		if(title.equals(Titles.Map_Size_Y.toString())){
			mapSizeY = Integer.parseInt(value);
		}
	}
	  
}
