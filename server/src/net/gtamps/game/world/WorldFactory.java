package net.gtamps.game.world;

import java.io.IOException;

import net.gtamps.ResourceLoader;
import net.gtamps.game.physics.Box2DEngine;

import org.jdom.Element;
import org.jdom.JDOMException;

public class WorldFactory {

	private WorldFactory() {
	}
	
	public static World createMap(final Box2DEngine physics) {
		return new World("World",1024,768,physics);
	}
	
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
	
		

	
}
