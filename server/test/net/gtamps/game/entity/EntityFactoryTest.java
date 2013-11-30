package net.gtamps.game.entity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.level.PhysicalShape;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EntityFactoryTest {
	
	@Mock
	private Box2DEngine physics;
	@Mock
	private World world;
	@Mock
	Body body;
	
	private Universe universe;

	@Before
	public void createUniverse() {
		universe = new Universe("testuniverse", 100, 100);
		universe.setPhysics(physics);
		
	}

	@Test
	public void testCreateSpecialEntityHouse_whenValidShape_shouldCreateEntityNamedHouse() {
		// setup
		when(physics.getWorld()).thenReturn(world);
		when(world.createBody(isA(BodyDef.class))).thenReturn(body);
		final PhysicalShape pshape = new PhysicalShape();
		
		// run
		final Entity entity = EntityFactory.createSpecialEntityHouse(universe, pshape);
		
		// assert
		assertTrue("created entity has wrong name", "house".equalsIgnoreCase(entity.getName()));
	}
	
	@Test
	public void testCreateSpecialEntityHouse_whenValidShape_shouldCreateEntityWithSimplePhysicsHandler() {
		// setup
		when(physics.getWorld()).thenReturn(world);
		when(world.createBody(isA(BodyDef.class))).thenReturn(body);
		final PhysicalShape pshape = new PhysicalShape();
		
		// run
		final Entity entity = EntityFactory.createSpecialEntityHouse(universe, pshape);
		
		// assert
		final SimplePhysicsHandler physh = (SimplePhysicsHandler) entity.getHandler(Handler.Type.PHYSICS);
		assertNotNull("created entity has no physics handler", physh);
	}

	@Test
	public void testCreateSpecialEntityHouse_whenValidShape_shouldCreateAPhysicsBodyShape() {
		// setup
		when(physics.getWorld()).thenReturn(world);
		when(world.createBody(isA(BodyDef.class))).thenReturn(body);
		final PhysicalShape pshape = new PhysicalShape();
//		pshape.add(Vector3.createNew(0f, 0f, 0f));
//		pshape.add(Vector3.createNew(1f, 1f, 0f));
//		pshape.add(Vector3.createNew(1f, 0f, 0f));
		
		// run
		EntityFactory.createSpecialEntityHouse(universe, pshape);
		
		// assert
		verify(world, atLeastOnce()).createBody(isA(BodyDef.class));
		verify(body).createShape(isA(PolygonDef.class));
	}

}
