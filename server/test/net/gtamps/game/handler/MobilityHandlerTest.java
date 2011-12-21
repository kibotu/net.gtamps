package net.gtamps.game.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtamps.game.physics.MobilityProperties;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MobilityHandlerTest {

	@Mock
	private ConcurrentLinkedQueue<GameEvent> actionQueue;

	@Mock
	private Universe universe;

	@Mock
	private Body body;

	@Mock
	private MobilityProperties mobilityProperties;

	@Mock
	private Entity parent;

	@Mock
	private SimplePhysicsHandler physicsHandler;

	@Mock
	private World world;

	private MobilityHandler mobilityHandler;

	@Before
	public void createMobilityHandler() throws Exception {
		mobilityHandler = new MobilityHandler(universe, parent, mobilityProperties,physicsHandler);
		mobilityHandler.actionQueue = actionQueue;
		mobilityHandler.body = body;
		mobilityHandler.world = world;
	}

	@Test
	public void testMobilityHandler_constructor_shouldSetEnabledToTrue() {
		// setup
		MobilityHandler mobHandler;

		// run
		mobHandler = new MobilityHandler(universe, parent, mobilityProperties, physicsHandler);

		// assert
		assertTrue(mobHandler.isEnabled());
	}

	@Test
	public void testReceiveEvent_ActionEvent_ShouldFillActionQueue() {
		// setup
		final GameEvent actionEvent = new GameEvent(EventType.ACTION_ACCELERATE, parent);

		// run
		mobilityHandler.receiveEvent(actionEvent);

		// assert
		verify(actionQueue).add(actionEvent);
	}

	@Test
	public void testReceiveEvent_UpdateEvent_ShouldQueryActionQueue() {
		// setup
		final GameEvent updateEvent = new GameEvent(EventType.SESSION_UPDATE, parent);
		when(body.getLinearVelocity()).thenReturn(new Vec2(0, 0));
		when(actionQueue.isEmpty()).thenReturn(true);

		// run
		mobilityHandler.receiveEvent(updateEvent);

		// assert
		verify(actionQueue, atLeastOnce()).isEmpty();
	}

	@Test
	public void testDisable_disable_ShouldSetEnabledToFalse() {
		// setup

		// run
		mobilityHandler.disable();

		// assert
		assertFalse(mobilityHandler.isEnabled());
	}


	@Test
	public void testEnable_enable_ShouldSetEnabledToTrueAndDispatchEvents() {
		// setup
		mobilityHandler.disable();


		// run
		mobilityHandler.enable();

		// assert
		assertTrue(mobilityHandler.isEnabled());
	}



	@Test
	public void testAddAction_addAction_shouldFillActionQueue() {
		// setup
		final GameEvent actionEvent = new GameEvent(EventType.ACTION_ACCELERATE, parent);

		// run
		mobilityHandler.addAction(actionEvent);

		// assert
		verify(actionQueue).add(actionEvent);
	}

	@Test
	public void testUpdate_update_shouldQueryActionQueue() {
		// setup
		final GameEvent updateEvent = new GameEvent(EventType.SESSION_UPDATE, parent);
		when(body.getLinearVelocity()).thenReturn(new Vec2(0, 0));
		when(actionQueue.isEmpty()).thenReturn(true);

		// run
		mobilityHandler.receiveEvent(updateEvent);

		// assert
		verify(actionQueue, atLeastOnce()).isEmpty();

	}

}
