package gtamapedit.file;

import gtamapedit.conf.Configuration;
import gtamapedit.tileManager.TileImageHolder;
import gtamapedit.view.map.MapElement;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class TileBitmapBuilder {
	public static BufferedImage createTileMap(MapFile mapFile){
		
		LinkedList<TileImageHolder> tileImagesDistinct = new LinkedList<TileImageHolder>();
		HashMap<TileImageHolder, Point2D> imageMapping = new HashMap<TileImageHolder, Point2D>();
		
		MapElement[][] me = mapFile.getMap();
		for (int x = 0; x < me.length; x++) {
			for (int y = 0; y < me[0].length; y++) {
				if(!tileImagesDistinct.contains(me[x][y].getTextureTop()) && 
						me[x][y].getTextureTop() != null){
					tileImagesDistinct.add(me[x][y].getTextureTop());
				}
				if(!tileImagesDistinct.contains(me[x][y].getTextureEast()) && 
						me[x][y].getTextureEast() != null){
					tileImagesDistinct.add(me[x][y].getTextureEast());
				}
				if(!tileImagesDistinct.contains(me[x][y].getTextureNorth()) && 
						me[x][y].getTextureNorth() != null){
					tileImagesDistinct.add(me[x][y].getTextureNorth());
				}
				if(!tileImagesDistinct.contains(me[x][y].getTextureSouth()) && 
						me[x][y].getTextureSouth() != null){
					tileImagesDistinct.add(me[x][y].getTextureSouth());
				}
				if(!tileImagesDistinct.contains(me[x][y].getTextureWest()) && 
						me[x][y].getTextureWest() != null){
					tileImagesDistinct.add(me[x][y].getTextureWest());
				}
			}
		}
		
		int tileMapSize = (int) Math.ceil(Math.sqrt(tileImagesDistinct.size()));
		System.out.println(tileMapSize);	
		int ts = Configuration.tileSize;
		BufferedImage tileMap = new BufferedImage(tileMapSize*ts, tileMapSize*ts, BufferedImage.TYPE_INT_RGB);
		
		int i = 0;
		for(TileImageHolder t : tileImagesDistinct){
			Graphics2D g2d = tileMap.createGraphics();
			g2d.drawImage(t.getTileImage(), (i%tileMapSize)*ts, (i/tileMapSize)*ts, null);
			i++;
		}
		JDialog dialog = new JDialog();
		JLabel bild = new JLabel();
		bild.setIcon(new ImageIcon(tileMap));
		dialog.add(bild);
		dialog.setSize(tileMapSize+30,tileMapSize+30);
		dialog.setVisible(true);
		return tileMap;
	}
}
