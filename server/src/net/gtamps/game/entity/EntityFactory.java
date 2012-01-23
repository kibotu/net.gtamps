package net.gtamps.game.entity;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.handler.DriverHandler;
import net.gtamps.game.handler.SensorDoorHandler;
import net.gtamps.game.handler.ShootingHandler;
import net.gtamps.game.handler.blueprints.CollisionDamageBlueprint;
import net.gtamps.game.handler.blueprints.GenericHandlerBlueprint;
import net.gtamps.game.handler.blueprints.HealthBlueprint;
import net.gtamps.game.handler.blueprints.MobilityBlueprint;
import net.gtamps.game.handler.blueprints.PhysicsBlueprint;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.level.PhysicalShape;

import org.jbox2d.dynamics.World;

public class EntityFactory {

	@SuppressWarnings("unused")
	private static LogType TAG = LogType.GAMEWORLD;

	//TODO refactor to get rid of physics.World parameters
	//TODO don't do pix-to-world conversion here


	private static final Map<String, EntityBlueprint> blueprints = new HashMap<String, EntityBlueprint>(10);

	private EntityFactory() {
	}

	private static EntityBlueprint createBlueprint(final Universe universe, final String normName) {
		final EntityBlueprint blup = EntityBlueprint.getDisposableBlueprint(normName);
		final PhysicalProperties physprop;
		if (normName.equals("CAR")) {
			physprop = PhysicalProperties.Sportscar;
			blup.addHandlerPrototype(new GenericHandlerBlueprint<DriverHandler>(universe, DriverHandler.class, Handler.Type.DRIVER));
			blup.addHandlerPrototype(new CollisionDamageBlueprint(universe, 0, 1f));
		} else if (normName.equals("HUMAN")) {
			physprop = PhysicalProperties.Human;
			blup.addHandlerPrototype(new HealthBlueprint(universe, 100, 1f, 1));
			blup.addHandlerPrototype(new GenericHandlerBlueprint<SensorDoorHandler>(universe, SensorDoorHandler.class, Handler.Type.SENSOR));
			blup.addHandlerPrototype(new GenericHandlerBlueprint<ShootingHandler>(universe, ShootingHandler.class, Handler.Type.SHOOTING));
		} else if (normName.equals("BULLET")) {
			physprop = PhysicalProperties.Bullet;
			blup.addHandlerPrototype(new CollisionDamageBlueprint(universe, 10, 1f));
		} else if (normName.equals("SPAWNPOINT")) {
			physprop = PhysicalProperties.Empty;
		} else {
			physprop = PhysicalProperties.Empty;
		}
		final PhysicsBlueprint physicsBlup = PhysicsFactory.createPhysicsBlueprint(universe, physprop);
		blup.addHandlerPrototype(physicsBlup);
		final MobilityBlueprint mobilityBlup = PhysicsFactory.createMobilityBlueprint(universe, physprop);
		if (mobilityBlup != null) {
			blup.addHandlerPrototype(mobilityBlup);
		}
		return blup;
	}

	private static EntityBlueprint getCachedBlueprint(final Universe universe, final String normName) {
		final EntityBlueprint blup;
		if (blueprints.containsKey(normName)) {
			blup = blueprints.get(normName);
		} else {
			blup = createBlueprint(universe, normName);
			blueprints.put(normName, blup);
		}
		return blup;
	}

	private static EntityBlueprint getEmptyDisposableBlueprint(final Universe universe, final String normName) {
		return EntityBlueprint.getDisposableBlueprint(normName);
	}

	public static Entity createEntity(final Universe universe, final String name, final int pixX, final int pixY, final int deg) {
		final String normName = Entity.normalizeName(name);
		final Entity e = getCachedBlueprint(universe, normName).createEntity(pixX, pixY, deg);
		return e;
	}

	//TODO de-uglify
	public static Entity createSpecialEntityHouse(final Universe universe, final PhysicalShape pshape) {
		final String houseName = "house";
		final String normName = Entity.normalizeName(houseName);
		final EntityBlueprint houseblup = getEmptyDisposableBlueprint(universe, normName);
		houseblup.addHandlerPrototype(PhysicsFactory.createHouseBlueprintFromLevelPhysicalShape(universe, pshape));
		houseblup.addHandlerPrototype(new CollisionDamageBlueprint(universe, 0, 1f));
		final Entity house =  houseblup.createEntity(0, 0, 0);
		house.setSilent(true);
		return house;
	}


	/**
	 * @deprecated see {@link #createEntity(World, String, int, int, int)}
	 */
	@Deprecated
	public static Entity createEntityCar(final Universe universe, final int pixX, final int pixY, final int deg) {
		final Entity entity = createEntity(universe, "car", pixX, pixY, deg);
		return entity;
	}

	/**
	 * @deprecated see {@link #createEntity(World, String, int, int, int)}
	 */
	@Deprecated
	public static Entity createEntityHuman(final Universe universe, final int pixX, final int pixY, final int deg, final EntityManager em) {
		final Entity entity = createEntity(universe, "human", pixX, pixY, deg);
		return entity;
	}

	/**
	 * @deprecated see {@link #createEntity(World, String, int, int, int)}
	 */
	@Deprecated
	public static Entity createEntityHouse(final Universe universe, final int pixX, final int pixY) {
		final Entity entity = createEntity(universe, "house", pixX, pixY, 0);
		return entity;
	}

	/**
	 * @deprecated see {@link #createEntity(World, String, int, int, int)}
	 */
	@Deprecated
	public static Entity createEntitySpawnPoint(final Universe universe, final int pixX, final int pixY, final Integer deg) {
		final Entity entity = createEntity(universe, "spawnpoint", pixX, pixY, deg);
		return entity;
	}

	/**
	 * @deprecated see {@link #createEntity(World, String, int, int, int)}
	 */
	@Deprecated
	public static Entity createEntityBullet(final Universe universe, int pixX, int pixY, final int rotation, final int launchDistance) {
		pixX += Math.cos(rotation / 180.0 * Math.PI) * launchDistance;
		pixY += Math.sin(rotation / 180.0 * Math.PI) * launchDistance;
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
		final Entity entity = createEntity(universe, "bullet", pixX, pixY, rotation);
		return entity;
	}

}
