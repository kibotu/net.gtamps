package gtamapedit.conf;

public class Configuration {
	private static String tileImagePath = "/home/tom/studium/mmp2/mapedit/tile_images";
	private static String savePath = "/home/tom/studium/mmp2/mapedit/map1.map";
	private static int maxFloors = 10;
	private static int mapSizeX = 20;
	private static int mapSizeY = 20;
	
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
	public static void setSavePath(String savePath) {
		Configuration.savePath = savePath;
	}
}
