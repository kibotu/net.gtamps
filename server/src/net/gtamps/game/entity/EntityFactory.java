package net.gtamps.game.entity;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.handler.DriverHandler;
import net.gtamps.game.handler.HealthHandler;
import net.gtamps.game.handler.PhysicsHandler;
import net.gtamps.game.handler.SensorExplosionHandler;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class EntityFactory {
	
	private static LogType TAG = LogType.GAMEWORLD;

	//TODO refactor to get rid of physics.World parameters
	//TODO don't do pix-to-world conversion here
	
	private EntityFactory() {
	}

	public static Entity createEntityCar(final World world, final int pixX, final int pixY, final int deg) {
//		float x = pixX / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		float y = pixY / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		float rotation = (float) ((deg / 180.0f) * Math.PI);
		
		final Body carBody = PhysicsFactory.createSportsCar(world, PhysicalProperties.Sportscar, pixX, pixY, deg);
		Logger.i().log(TAG,"Created car at position x:"+pixX+" y:"+pixY);
		final Entity entity = new Entity("car");
		carBody.setUserData(entity);
//		entity.addProperty(new PositionProperty(pixX,pixY,deg, entity));
//		entity.addProperty(new SpeedProperty(entity));
//		entity.addProperty(new HealthProperty(entity, 2000));
//		entity.addProperty(new IncarnationProperty(entity));
		final PhysicsHandler physicsHandler = new PhysicsHandler(entity, carBody, PhysicalProperties.Sportscar);
		entity.setHandler(physicsHandler);
		entity.setHandler(new DriverHandler(entity));
		entity.setHandler(new SensorExplosionHandler(entity, 5000f));
		return entity;
	}
	
	public static Entity createEntityHuman(final World world, final int pixX, final int pixY, final int deg, final EntityManager em) {
		final Body humanBody = PhysicsFactory.createHuman(world, PhysicalProperties.Human, pixX, pixY, deg);
		Logger.i().log(TAG,"Created human at position x:"+pixX+" y:"+pixY);
		final Entity entity = new Entity("human");
		humanBody.setUserData(entity);
//		entity.addProperty(new PositionProperty(pixX,pixY,deg, entity));
//		entity.addProperty(new SpeedProperty(entity));
//		entity.addProperty(new HealthProperty(entity, 200));
//		entity.addProperty(new IncarnationProperty(entity));
//		entity.addProperty(new ActivationProperty(entity));
		final PhysicsHandler physicsHandler = new PhysicsHandler(entity, humanBody, PhysicalProperties.Human);
		final Handler healthHandler = new HealthHandler(entity, 100, 0f, 0);
		entity.setHandler(physicsHandler);
		entity.setHandler(healthHandler);
		//entity.setHandler(new SensorDoorHandler(entity));
		//entity.setHandler(new ShootingHandler(entity, em));
		return entity;
	}
	
	public static Entity createEntityHouse(final World world, final int pixX, final int pixY) {

		final Body houseBody = PhysicsFactory.createHouse(world, pixX, pixY);
		final Entity entity = new Entity("house");
		houseBody.setUserData(entity);
		return entity;
	}
	
	public static Entity createEntitySpawnPoint(final World world, final int pixX, final int pixY, final Integer rotation) {
//		float x = pixX / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		float y = pixY / WorldConstants.PIX_TO_PHYSICS_RATIO;
		final Body spawnBody = PhysicsFactory.createSpawnPoint(world, pixX, pixY, rotation);
		final Entity entity = new Entity("spawnpoint");
		spawnBody.setUserData(entity);
		entity.setSilent(true);
//		entity.addProperty(new PositionProperty(pixX,pixY,0, entity));
		entity.x.set(pixX);
		entity.y.set(pixY);
		entity.rota.set(rotation);
		// TODO collisionHandler
		return entity;
	}
	
	public static Entity createEntityBullet(final World world, int pixX, int pixY, final int rotation, final int launchDistance) {
		pixX += Math.cos(rotation/180.0*Math.PI)*launchDistance;
		pixY += Math.sin(rotation/180.0*Math.PI)*launchDistance;
		final Body bulletBody = PhysicsFactory.createBullet(world, PhysicalProperties.Bullet, pixX, pixY, rotation);
		Logger.i().log(TAG,"Created bullet at position x:"+pixX+" y:"+pixY);
		final Entity entity = new Entity("bullet");
		bulletBody.setUserData(entity);
//		entity.addProperty(new PositionProperty(pixX, pixY, 0, entity));
//		entity.addProperty(new SpeedProperty(entity));
//		entity.addProperty(new HealthProperty(entity, 10));
//		entity.addProperty(new ActivationProperty(entity));
		final SimplePhysicsHandler physicsHandler = new SimplePhysicsHandler(entity, bulletBody, PhysicalProperties.Bullet);
		entity.setHandler(physicsHandler);
		
		return entity;
	}
	
	
	
}
