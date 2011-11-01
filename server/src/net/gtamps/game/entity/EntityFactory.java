package net.gtamps.game.entity;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.handler.blueprints.HealthBlueprint;
import net.gtamps.game.handler.blueprints.MobilityBlueprint;
import net.gtamps.game.handler.blueprints.PhysicsBlueprint;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.entity.EntityBlueprint;

import org.jbox2d.dynamics.World;

public class EntityFactory {
	
	private static LogType TAG = LogType.GAMEWORLD;

	//TODO refactor to get rid of physics.World parameters
	//TODO don't do pix-to-world conversion here
	
	
	private static final Map<String, EntityBlueprint> blueprints = new HashMap<String, EntityBlueprint>(10);
	
	private EntityFactory() {
	}

	private static EntityBlueprint createBlueprint(final World world, final String normName) {
		final EntityBlueprint blup = EntityBlueprint.get(normName);
		final PhysicalProperties physprop;
		if (normName.equals("CAR")) {
			physprop = PhysicalProperties.Sportscar;
		} else if (normName.equals("HUMAN")) {
			physprop = PhysicalProperties.Human;
			blup.addHandlerPrototype(new HealthBlueprint(100, 1f, 1));
		} else if (normName.equals("BULLET")) {
			physprop = PhysicalProperties.Bullet;
		} else {
			physprop = PhysicalProperties.Empty;
		}
		final PhysicsBlueprint physicsBlup = PhysicsFactory.createPhysicsBlueprint(world, physprop);
		blup.addHandlerPrototype(physicsBlup);
		final MobilityBlueprint mobilityBlup = PhysicsFactory.createMobilityBlueprint(world, physprop);
		if (mobilityBlup != null) {
			blup.addHandlerPrototype(mobilityBlup);
		}
		return blup;
	}
	
	private static EntityBlueprint getBlueprint(final World world, final String normName) {
		final EntityBlueprint blup;
		if (blueprints.containsKey(normName)) {
			blup = blueprints.get(normName);
		} else {
			blup = createBlueprint(world, normName);
			blueprints.put(normName, blup);
		}
		return blup;
	}
	
	public static Entity createEntity(final World world, final String name, final int pixX, final int pixY, final int deg) {
		final String normName = Entity.normalizeName(name);
		return getBlueprint(world, normName).createEntity(pixX, pixY, deg);
	}
	
	
	
	public static Entity createEntityCar(final World world, final int pixX, final int pixY, final int deg) {
////		float x = pixX / WorldConstants.PIX_TO_PHYSICS_RATIO;
////		float y = pixY / WorldConstants.PIX_TO_PHYSICS_RATIO;
////		float rotation = (float) ((deg / 180.0f) * Math.PI);
//
//		final Body carBody = PhysicsFactory.createSportsCar(world, PhysicalProperties.Sportscar, pixX, pixY, deg);
//		Logger.i().log(TAG,"Created car at position x:"+pixX+" y:"+pixY);
//		final Entity entity = new Entity("car");
//		carBody.setUserData(entity);
////		entity.addProperty(new PositionProperty(pixX,pixY,deg, entity));
////		entity.addProperty(new SpeedProperty(entity));
////		entity.addProperty(new HealthProperty(entity, 2000));
////		entity.addProperty(new IncarnationProperty(entity));
//		final PhysicsHandler physicsHandler = new PhysicsHandler(entity, carBody, PhysicalProperties.Sportscar);
//		entity.setHandler(physicsHandler);
//		entity.setHandler(new DriverHandler(entity));
//		entity.setHandler(new SensorExplosionHandler(entity, 5000f));
		final Entity entity = createEntity(world, "car", pixX, pixY, deg);
		return entity;
	}
	
	public static Entity createEntityHuman(final World world, final int pixX, final int pixY, final int deg, final EntityManager em) {
//		final Body humanBody = PhysicsFactory.createHuman(world, PhysicalProperties.Human, pixX, pixY, deg);
//		Logger.i().log(TAG,"Created human at position x:"+pixX+" y:"+pixY);
//		final Entity entity = new Entity("human");
//		humanBody.setUserData(entity);
////		entity.addProperty(new PositionProperty(pixX,pixY,deg, entity));
////		entity.addProperty(new SpeedProperty(entity));
////		entity.addProperty(new HealthProperty(entity, 200));
////		entity.addProperty(new IncarnationProperty(entity));
////		entity.addProperty(new ActivationProperty(entity));
//		final PhysicsHandler physicsHandler = new PhysicsHandler(entity, humanBody, PhysicalProperties.Human);
//		final Handler healthHandler = new HealthHandler(entity, 100, 1f, 0);
//		entity.setHandler(physicsHandler);
//		entity.setHandler(healthHandler);
//		//entity.setHandler(new SensorDoorHandler(entity));
//		//entity.setHandler(new ShootingHandler(entity, em));
		final Entity entity = createEntity(world, "human", pixX, pixY, deg);
		return entity;
	}
	
	public static Entity createEntityHouse(final World world, final int pixX, final int pixY) {
//
//		final Body houseBody = PhysicsFactory.createHouse(world, pixX, pixY);
//		final Entity entity = new Entity("house");
//		houseBody.setUserData(entity);
		final Entity entity = createEntity(world, "house", pixX, pixY, 0);
		return entity;
	}
	
	public static Entity createEntitySpawnPoint(final World world, final int pixX, final int pixY, final Integer deg) {
////		float x = pixX / WorldConstants.PIX_TO_PHYSICS_RATIO;
////		float y = pixY / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		final Body spawnBody = PhysicsFactory.createSpawnPoint(world, pixX, pixY, rotation);
//		final Entity entity = new Entity("spawnpoint");
//		spawnBody.setUserData(entity);
//		entity.setSilent(true);
////		entity.addProperty(new PositionProperty(pixX,pixY,0, entity));
//		entity.x.set(pixX);
//		entity.y.set(pixY);
//		entity.rota.set(rotation);
//		// TODO collisionHandler
		final Entity entity = createEntity(world, "spawnpoint", pixX, pixY, deg);
		return entity;
	}
	
	public static Entity createEntityBullet(final World world, int pixX, int pixY, final int rotation, final int launchDistance) {
		pixX += Math.cos(rotation/180.0*Math.PI)*launchDistance;
		pixY += Math.sin(rotation/180.0*Math.PI)*launchDistance;
//		final Body bulletBody = PhysicsFactory.createBullet(world, PhysicalProperties.Bullet, pixX, pixY, rotation);
//		Logger.i().log(TAG,"Created bullet at position x:"+pixX+" y:"+pixY);
//		final Entity entity = new Entity("bullet");
//		bulletBody.setUserData(entity);
////		entity.addProperty(new PositionProperty(pixX, pixY, 0, entity));
////		entity.addProperty(new SpeedProperty(entity));
////		entity.addProperty(new HealthProperty(entity, 10));
////		entity.addProperty(new ActivationProperty(entity));
//		final SimplePhysicsHandler physicsHandler = new SimplePhysicsHandler(entity, bulletBody, PhysicalProperties.Bullet);
//		entity.setHandler(physicsHandler);
		final Entity entity = createEntity(world, "bullet", pixX, pixY, rotation);
		return entity;
	}
	
	
	
}
