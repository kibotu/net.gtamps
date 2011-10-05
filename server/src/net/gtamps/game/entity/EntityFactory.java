package net.gtamps.game.entity;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.handler.DriverHandler;
import net.gtamps.game.handler.PhysicsHandler;
import net.gtamps.game.handler.SensorDoorHandler;
import net.gtamps.game.handler.SensorExplosionHandler;
import net.gtamps.game.handler.ShootingHandler;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.property.ActivationProperty;
import net.gtamps.game.property.HealthProperty;
import net.gtamps.game.property.IncarnationProperty;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.SpeedProperty;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class EntityFactory {
	
	private static LogType TAG = LogType.GAMEWORLD;

	//TODO refactor to get rid of physics.World parameters
	//TODO don't do pix-to-world conversion here
	
	private EntityFactory() {
	}

	public static Entity createEntityCar(World world, int pixX, int pixY, int deg) {
//		float x = pixX / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		float y = pixY / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		float rotation = (float) ((deg / 180.0f) * Math.PI); 
		
		Body carBody = PhysicsFactory.createSportsCar(world, PhysicalProperties.Sportscar, pixX, pixY, deg);
		Logger.i().log(TAG,"Created car at position x:"+pixX+" y:"+pixY);
		Entity entity = new Entity("car");
		carBody.setUserData(entity);
		entity.addProperty(new PositionProperty(pixX,pixY,deg, entity));
		entity.addProperty(new SpeedProperty(entity));
		entity.addProperty(new HealthProperty(entity, 2000));
		entity.addProperty(new IncarnationProperty(entity));
		PhysicsHandler physicsHandler = new PhysicsHandler(entity, carBody, PhysicalProperties.Sportscar); 
		entity.setPhysicsHandler(physicsHandler);
		entity.setHandler(new DriverHandler(entity));
		entity.setHandler(new SensorExplosionHandler(entity, 5000f));
		return entity;
	}
	
	public static Entity createEntityHuman(World world, int pixX, int pixY, int deg, EntityManager em) {
		Body humanBody = PhysicsFactory.createHuman(world, PhysicalProperties.Human, pixX, pixY, deg);
		Logger.i().log(TAG,"Created human at position x:"+pixX+" y:"+pixY);
		Entity entity = new Entity("human");
		humanBody.setUserData(entity);
		entity.addProperty(new PositionProperty(pixX,pixY,deg, entity));
		entity.addProperty(new SpeedProperty(entity));
		entity.addProperty(new HealthProperty(entity, 200));
		entity.addProperty(new IncarnationProperty(entity));
		entity.addProperty(new ActivationProperty(entity));
		PhysicsHandler physicsHandler = new PhysicsHandler(entity, humanBody, PhysicalProperties.Human); 
		entity.setPhysicsHandler(physicsHandler);
		entity.setHandler(new SensorDoorHandler(entity));
		entity.setHandler(new ShootingHandler(entity, em));
		return entity;
	}
	
	public static Entity createEntityHouse(World world, int pixX, int pixY) {

		Body houseBody = PhysicsFactory.createHouse(world, pixX, pixY);
		Entity entity = new Entity("house");
		houseBody.setUserData(entity);
		return entity;
	}
	
	public static Entity createEntitySpawnPoint(World world, int pixX, int pixY, Integer rotation) {
//		float x = pixX / WorldConstants.PIX_TO_PHYSICS_RATIO;
//		float y = pixY / WorldConstants.PIX_TO_PHYSICS_RATIO;
		Body spawnBody = PhysicsFactory.createSpawnPoint(world, pixX, pixY, rotation);
		Entity entity = new Entity("spawnpoint");
		spawnBody.setUserData(entity);
		entity.setSilent(true);
		entity.addProperty(new PositionProperty(pixX,pixY,0, entity));
		// TODO collisionHandler
		return entity;
	}
	
	public static Entity createEntityBullet(World world, int pixX, int pixY, int rotation, int launchDistance) {
		pixX += Math.cos(rotation/180.0*Math.PI)*launchDistance;
		pixY += Math.sin(rotation/180.0*Math.PI)*launchDistance;
		Body bulletBody = PhysicsFactory.createBullet(world, PhysicalProperties.Bullet, pixX, pixY, rotation);
		Logger.i().log(TAG,"Created bullet at position x:"+pixX+" y:"+pixY);
		Entity entity = new Entity("bullet");
		bulletBody.setUserData(entity);
		entity.addProperty(new PositionProperty(pixX, pixY, 0, entity));
		entity.addProperty(new SpeedProperty(entity));
		entity.addProperty(new HealthProperty(entity, 10));
		entity.addProperty(new ActivationProperty(entity));
		SimplePhysicsHandler physicsHandler = new SimplePhysicsHandler(entity, bulletBody, PhysicalProperties.Bullet); 
		entity.setPhysicsHandler(physicsHandler);
		
		return entity;
	}
	
	
	
}
