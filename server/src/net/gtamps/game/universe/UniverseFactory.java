package net.gtamps.game.universe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import net.gtamps.ResourceLoader;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.level.Level;

import org.jdom.Element;
import org.jdom.JDOMException;

public class UniverseFactory {

	private UniverseFactory() {
	}
	
	/** atm, creates an empty world with size 1024x768 */
	public static Universe createMap() {
		return new Universe("World",1024,768);
	}
	
	/** use to load a map in the old (flash-era) xml format */
	public static Universe loadMap(final String name) {
		Universe world;
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
	public static Universe loadWorldFromLevel(final String path) {
		InputStream input;
		try {
			input = ResourceLoader.getFileAsInputStream(path);
		} catch (final FileNotFoundException e) {
			Logger.e(Level.class, "cannot load level: file not found! " + path);
			return null;
		}
		final Level level = Level.loadLevel(input);
		final Universe world = LevelParser.buildWorldFromLevel(level);
		throw new NoSuchElementException("WorldFactory.loadWorldFromLevel is not fully implemented yet");
	}
	
		

	
}
