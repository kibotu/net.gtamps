package net.gtamps.shared.game.handler;

import static org.mockito.Mockito.*;
import net.gtamps.shared.game.IGameActor;
import net.gtamps.shared.game.NullGameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;
import net.gtamps.shared.game.handler.Handler.Type;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

	private static final EventType UNAMBIGUOUS_EVENT_TYPE = EventType.ACTION_ACCELERATE;
	private static final EventType DIFFERENT_UNAMBIGUOUS_EVENT_TYPE = EventType.SESSION_UPDATE;
	private static final GameEvent SAMPLE_EVENT = new GameEvent(UNAMBIGUOUS_EVENT_TYPE, NullGameObject.DUMMY);
	private static final GameEvent DIFFERENT_EVENT = new GameEvent(UNAMBIGUOUS_EVENT_TYPE, NullGameObject.DUMMY);

	@Mock
	private Entity parent;

	private final Type type = Handler.Type.DRIVER;
	private Handler handler;

	@Before
	public void createHandler() throws Exception {
		handler = new Handler(type, parent);
	}

	@Test
	public void testaddEventListenerDispatchEvent_shouldPassEventsToEventListener() {
		// setup
		final IGameEventListener listener = parent;
		handler.enable();

		// run
		handler.addEventListener(UNAMBIGUOUS_EVENT_TYPE, listener);
		handler.dispatchEvent(SAMPLE_EVENT);

		// assert
		verify(listener).receiveEvent(SAMPLE_EVENT);
	}


	@Test
	public void testRemoveEventListener_shouldStopEventsBeingPassed() {
		// setup
		final IGameEventListener listener = parent;
		handler.addEventListener(UNAMBIGUOUS_EVENT_TYPE, listener);
		handler.enable();

		// run
		handler.removeEventListener(UNAMBIGUOUS_EVENT_TYPE, listener);
		handler.dispatchEvent(SAMPLE_EVENT);

		// assert
		verify(parent, never()).receiveEvent(isA(GameEvent.class));
	}

	@Test
	public void testConnectUpwardsActor_whenReceivesDownNotEmpty_shouldRegisterAsEventListenerWithActor() {
		// setup
		final IGameActor upwardsActor = parent;
		handler.setReceives(new EventType[] {UNAMBIGUOUS_EVENT_TYPE});

		// run
		handler.connectUpwardsActor(upwardsActor);

		// assert
		verify(upwardsActor, atLeastOnce()).addEventListener(isA(EventType.class), same(handler));
	}


	@Test
	public void testDisconnectUpwardsActor_shouldRemoveSelfAsListenerFromActor() {
		// setup
		final IGameActor upwardsActor = parent;
		handler.setReceives(new EventType[] {UNAMBIGUOUS_EVENT_TYPE});
		handler.connectUpwardsActor(upwardsActor);

		// run
		handler.disconnectUpwardsActor(upwardsActor);

		// assert
		verify(upwardsActor, atLeastOnce()).removeEventListener(isA(EventType.class), same(handler));
	}

	@Test
	public void testSetReceives_shouldChangeWhichEventTypesListenedFor() {
		// setup
		final IGameActor upwardsActor = parent;

		// run
		handler.setReceives(new EventType[] {UNAMBIGUOUS_EVENT_TYPE});
		handler.connectUpwardsActor(upwardsActor);

		// assert
		verify(upwardsActor, atLeastOnce()).addEventListener(same(UNAMBIGUOUS_EVENT_TYPE), same(handler));

		//setup again
		handler.disconnectUpwardsActor(upwardsActor);

		// run again
		handler.setReceives(new EventType[] {DIFFERENT_UNAMBIGUOUS_EVENT_TYPE});
		handler.connectUpwardsActor(upwardsActor);

		// new assertions
		verify(upwardsActor, atLeastOnce()).addEventListener(same(DIFFERENT_UNAMBIGUOUS_EVENT_TYPE), same(handler));
	}

}
