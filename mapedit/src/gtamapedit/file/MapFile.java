package gtamapedit.file;

import gtamapedit.conf.Configuration;
import gtamapedit.view.map.MapElement;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.game.level.EntityPosition;
import net.gtamps.shared.game.level.Level;
import net.gtamps.shared.game.level.PhysicalShape;


public class MapFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3459197974356309067L;
	private LinkedList<EntityPosition> entityPositions;
	private MapFileTileElement[][] mapData;

	public MapFile(String filename){
		loadMap(filename);
	}
	
	public MapFile(MapElement[][] map, LinkedList<EntityPosition> linkedList) {
		this.mapData = createSerializableMap(map);
		
		this.entityPositions = new LinkedList<EntityPosition>(linkedList);
	}

	public MapElement[][] getMap() {
		MapElement[][] map = new MapElement[mapData.length][mapData[0].length]; 
		for (int x = 0; x < mapData.length; x++) {
			for (int y = 0; y < mapData[0].length; y++) {
				map[x][y] = new MapElement(mapData[x][y]);
			}
			
		}
		return map;
	}

	private MapFileTileElement[][] createSerializableMap(MapElement[][] map){
		MapFileTileElement[][] mapData = new MapFileTileElement[map.length][map[0].length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				mapData[x][y] = new MapFileTileElement(map[x][y]);
			}
		}
		return mapData;
	}
	
	public MapFileTileElement[][] getRawData() {
		return mapData;
	}

	public LinkedList<EntityPosition> getEntityPositions() {
		return entityPositions;
	}

	public void setEntityPositions(LinkedList<EntityPosition> entityPositions) {
		this.entityPositions = entityPositions;
	}

	public void saveMap(String filename) {
		TileBitmapBuilder tileImageBuilder = new TileBitmapBuilder(); 
		tileImageBuilder.createTileMap(this);
		tileImageBuilder.saveImage(filename+".png");
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
			fos.close();
			fos = new FileOutputStream(new File(filename + ".obj"));
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			PrintWriter pw = new PrintWriter(bos);
					
			pw.write(OBJBuilder.buildObjFromMap(this.getRawData(),tileImageBuilder.getImageMapping(),tileImageBuilder.getTextureSpacing()));
			pw.flush();
			pw.close();
			bos.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String exportMap(String filename) {
		Level level = new Level();
		LinkedList<PhysicalShape> phys = level.getPhysicalShapes();
		LinkedList<EntityPosition> entPos = level.getEntityPositions();
		
		TileBitmapBuilder tileImageBuilder = new TileBitmapBuilder(); 
		tileImageBuilder.createTileMap(this);

		/*
		 * Add all shapes to the level: the shapes are defined to be 2d on the
		 * x/y axis
		 */
		for (int x = 0; x < mapData.length; x++) {
			for (int y = 0; y < mapData[0].length; y++) {
				if (mapData[x][y].getFloors() > 0) {
					PhysicalShape shape = new PhysicalShape();
					shape.add(Vector3.createNew(x * Configuration.tileSize, y * Configuration.tileSize, 0));
					shape.add(Vector3.createNew(x * Configuration.tileSize + Configuration.tileSize, y
							* Configuration.tileSize, 0));
					shape.add(Vector3.createNew(x * Configuration.tileSize + Configuration.tileSize, y
							* Configuration.tileSize + Configuration.tileSize, 0));
					shape.add(Vector3.createNew(x * Configuration.tileSize, y * Configuration.tileSize
							+ Configuration.tileSize, 0));
					phys.add(shape);
				}
			}
		}
		/*
		 * Add all entities to the level: like spawnpoints, cars and redlights.
		 */
		for (EntityPosition ep : entityPositions) {
			entPos.add(ep);
		}
		/*
		 * Add all entities to the level: like spawnpoints, cars and redlights.
		 */
		for (int x = 0; x < mapData.length; x++) {
			for (int y = 0; y < mapData[0].length; y++) {
				if (mapData[x][y].getFloors() > 0) {

				}
			}
		}
		
		

		String objfile = OBJBuilder.buildObjFromMap(mapData,tileImageBuilder.getImageMapping(),tileImageBuilder.getTextureSpacing());
		level.set3DMap(objfile);

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(level);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "Writing export map file...\n" + "=== OBJ File size: " + objfile.length() + " Bytes\n"
				+ "=== Physical Objects: " + phys.size() + "\n" + "=== Entity Positions: " + entPos.size() + "\n";
	}

	public void loadMap(String path) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(new File(path));
			in = new ObjectInputStream(fis);
			MapFile mapFile = (MapFile) in.readObject();
			in.close();
			this.mapData = (MapFileTileElement[][]) mapFile.getRawData();
			this.entityPositions = mapFile.getEntityPositions();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}
}
