package net.gtamps.shared.game.level;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.LinkedList;

import net.gtamps.shared.Utils.Logger;

public class Level implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3756877185706183170L;
	
	LinkedList<PhysicalShape> physicalShapes = new LinkedList<PhysicalShape>();
	LinkedList<EntityPosition> entityPositions = new LinkedList<EntityPosition>();
	private String OBJMap = "";

	public LinkedList<PhysicalShape> getPhysicalShapes() {
		return physicalShapes;
	}

	public LinkedList<EntityPosition> getEntityPositions() {
		return entityPositions;
	}

	public void set3DMap(String ObjFromMap) {
		this.OBJMap = ObjFromMap;
	}
	
	public static Level loadLevel(InputStream is){
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(is);
			Level level = (Level) in.readObject();
			in.close();
			return level;
		} catch (StreamCorruptedException e) {
			Logger.e(Level.class, "Stream corrupted! Level loading failed!");
			return null;
		} catch (IOException e) {
			Logger.e(Level.class, "IO Exception! Level loading failed!");
			return null;
		} catch (ClassNotFoundException e) {
			Logger.e(Level.class, "Class not Found! Level loading failed!");
			return null;
		}
	}
}
