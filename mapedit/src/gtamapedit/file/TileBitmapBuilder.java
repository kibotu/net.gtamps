package gtamapedit.file;

import gtamapedit.conf.Configuration;
import gtamapedit.tileManager.TileImageHolder;
import gtamapedit.view.map.MapElement;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import ar.com.hjg.pngj.PngWriter;

public class TileBitmapBuilder {
	private LinkedList<TileImageHolder> tileImagesDistinct = new LinkedList<TileImageHolder>();
	private HashMap<String, Coordinate> imageMapping = new HashMap<String, Coordinate>();
	
	BufferedImage tileMap;
	
	private float textureSpacing = 1f; 
	TileBitmapBuilder(){
		
	}
	
	public LinkedList<TileImageHolder> getTileImagesDistinct() {
		return tileImagesDistinct;
	}
	public HashMap<String, Coordinate> getImageMapping() {
		return imageMapping;
	}
	
	public void createTileMap(MapFile mapFile){
		
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
		System.out.println("Map texture tiles: "+tileMapSize+"x"+tileMapSize);	
		int ts = Configuration.tileSize;
		tileMap = new BufferedImage(tileMapSize*ts, tileMapSize*ts, BufferedImage.TYPE_INT_RGB);
		
		textureSpacing = 1f/tileMapSize;
		
		int i = 0;
		for(TileImageHolder t : tileImagesDistinct){
			Graphics2D g2d = tileMap.createGraphics();
			g2d.drawImage(t.getTileImage(), (i%tileMapSize)*ts, (i/tileMapSize)*ts, null);
			
			imageMapping.put(t.getFilename(),new Coordinate((float)(i%tileMapSize)/tileMapSize, (float)(i/tileMapSize)/tileMapSize));
			System.out.println(t+": "+imageMapping.get(t.getFilename()));
			
			
			i++;
		}
		JDialog dialog = new JDialog();
		JLabel bild = new JLabel();
		bild.setIcon(new ImageIcon(tileMap));
		dialog.add(bild);
		dialog.setSize(tileMapSize*64,tileMapSize*64);
		dialog.setVisible(true);
		
		
 
	}
	public float getTextureSpacing() {
		return textureSpacing;
	}

	public void saveImage(String filename) {
		try {
			System.out.println("Saving tilemap to "+new File( filename ).getAbsolutePath());
			ImageIO.write( tileMap, "png", new File( filename ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
