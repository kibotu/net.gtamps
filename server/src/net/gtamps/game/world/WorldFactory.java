package net.gtamps.game.world;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import net.gtamps.ResourceLoader;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.level.Level;

import org.jdom.Element;
import org.jdom.JDOMException;

public class WorldFactory {

	private WorldFactory() {
	}
	
	/** atm, creates an empty world with size 1024x768 */
	public static World createMap(final Box2DEngine physics) {
		return new World("World",1024,768,physics);
	}
	
	/** use to load a map in the old (flash-era) xml format */
	public static World loadMap(final String name) {
		World world;
		Element mapXML;
		try {
			mapXML = ResourceLoader.getFileAsXml(name);
		} catch (final JDOMException e) {
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
		final MapParser mapParser = new MapParser(mapXML);
		world = mapParser.getWorld();
		mapParser.populateWorld(world.entityManager);
		return world;
	}
	
	/**
	 * builds a world by using a level file provided by our level editor
	 * 
	 * @param path	path to level file
	 * @return		world representation of level file
	 */
	public static World loadWorldFromLevel(final String path) {
		InputStream input;
		try {
			input = ResourceLoader.getFileAsInputStream(path);
		} catch (final FileNotFoundException e) {
			Logger.e(Level.class, "cannot load level: file not found! " + path);
			return null;
		}
		final Level level = Level.loadLevel(input);
		final World world = LevelParser.buildWorldFromLevel(level);
		throw new NoSuchElementException("WorldFactory.loadWorldFromLevel is not fully implemented yet");
	}
	
		

	
}
