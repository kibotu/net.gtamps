package net.gtamps.shared.game.level;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.LinkedList;

import net.gtamps.shared.Utils.Logger;

public class Level implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3756877185706183170L;

	int LevelWidthInPixelCoord = 0;
	int LevelHeightInPixelCoord = 0;
	LinkedList<PhysicalShape> physicalShapes = new LinkedList<PhysicalShape>();
	LinkedList<EntityPosition> entityPositions = new LinkedList<EntityPosition>();
	String levelName = "";
	private String OBJMap = "";



	public LinkedList<PhysicalShape> getPhysicalShapes() {
		return physicalShapes;
	}

	public LinkedList<EntityPosition> getEntityPositions() {
		return entityPositions;
	}

	public void set3DMap(final String ObjFromMap) {
		OBJMap = ObjFromMap;
	}

	public int getHeightInPixelCoord() {
		return LevelHeightInPixelCoord;
	}

	public int getWidthInPixelCoord() {
		return LevelWidthInPixelCoord;
	}

	public void setWidthInPixelCoord(final int levelWidthInPixelCoord) {
		LevelWidthInPixelCoord = levelWidthInPixelCoord;
	}

	public void setHeightInPixelCoord(final int levelHeightInPixelCoord) {
		LevelHeightInPixelCoord = levelHeightInPixelCoord;
	}

	public static Level loadLevel(final InputStream is) {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(is);
			final Level level = (Level) in.readObject();
			in.close();
			return level;
		} catch (final StreamCorruptedException e) {
			Logger.e(Level.class, "Stream corrupted! Level loading failed!");
			return null;
		} catch (final IOException e) {
			Logger.e(Level.class, "IO Exception! Level loading failed!");
			return null;
		} catch (final ClassNotFoundException e) {
			Logger.e(Level.class, "Class not Found! Level loading failed!");
			return null;
		}
	}

	public String getName() {
		return levelName;
	}
}
