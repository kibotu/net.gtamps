package net.gtamps.game.world;

import net.gtamps.game.physics.Box2DEngine;

public class WorldFactory {

	private WorldFactory() {
	}
	
	public static World createMap(Box2DEngine physics) {
		return new World("World",1024,768,physics);
	}
	
		

	
}
