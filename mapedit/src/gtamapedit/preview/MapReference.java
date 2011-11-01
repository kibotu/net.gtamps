package gtamapedit.preview;

import gtamapedit.file.MapFile;

public class MapReference {
	private static MapFile mf;
	public static void setMapFile(MapFile m){
		mf = m;
	}
	public static MapFile getMapFile(){
		return mf;
	}
}
