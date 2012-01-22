package net.gtamps.game.universe;

import net.gtamps.game.entity.EntityFactory;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.entity.EntityType;
import net.gtamps.shared.game.level.EntityPosition;
import net.gtamps.shared.game.level.Level;
import net.gtamps.shared.game.level.PhysicalShape;

public class LevelParser {

	private final Universe universe;
	private final Box2DEngine physics;

	private final Level level;

	
	public LevelParser(final Level level) {

		this.level = level;
		
		// determine world size
		final int mapWidth = level.getHeightInPixelCoord();
		final int mapHeight = level.getWidthInPixelCoord();

		universe = new Universe(level.getName(), mapWidth, mapHeight);
		physics = PhysicsFactory.createPhysics(universe, mapWidth, mapHeight);
		
		universe.setPhysics(physics);
	}
	
	public Universe buildWorldFromLevel() {
		final EntityManager entityManager = universe.entityManager;
		
		// create entities from entity positions
		for(final EntityPosition ep : level.getEntityPositions()){
			//TODO degrees!
			if(	ep.getType().equals(EntityType.CAR_CAMARO) ||
				ep.getType().equals(EntityType.CAR_CHEVROLET_CORVETTE) ||
				ep.getType().equals(EntityType.CAR_RIVIERA) ){
				entityManager.createEntityCar((int)ep.getPosition().x, (int)ep.getPosition().y, 0);
			}
			if( ep.getType().equals(EntityType.SPAWNPOINT)){
				final Entity spawnPoint = entityManager.createEntitySpawnPoint(universe, (int)ep.getPosition().x, (int)ep.getPosition().y, 0);
				universe.addSpawnPoint(spawnPoint);
			}
		}
		
		// create buildings from physical shapes
		for(final PhysicalShape shape : level.getPhysicalShapes()){
			final Entity e = EntityFactory.createSpecialEntityHouse(universe, shape);
			entityManager.registerEntity(e);
		}
		
		
		
		return universe;
	}

	public Box2DEngine getPhysics() {
		return physics;
	}

	public Universe getWorld() {
		return universe;
	}

}
