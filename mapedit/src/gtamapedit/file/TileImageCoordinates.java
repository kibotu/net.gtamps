package gtamapedit.file;

import java.awt.Point;
import java.awt.geom.Point2D;

public class TileImageCoordinates {
	int x;
	int y;
	int width;
	int height;
	int tileSize;
	
	TileImageCoordinates(int x, int y, int width, int height, int tileSize){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tileSize = tileSize;
	}
	public Coordinate[] getUVCoordinates(){
		Coordinate[] c = new Coordinate[4];
		c[0] = new Coordinate((float)width/(float)(x+tileSize),(float)height/(float)(y+tileSize));
		c[1] = new Coordinate((float)width/(float)x,(float)height/(float)(y+tileSize));
		c[2] = new Coordinate((float)width/(float)x,(float)height/(float)y);
		c[3] = new Coordinate((float)width/(float)(x+tileSize),(float)height/(float)y);
		return c;
	}
}
