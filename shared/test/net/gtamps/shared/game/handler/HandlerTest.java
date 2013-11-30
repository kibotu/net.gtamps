package net.gtamps.shared.game.handler;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Set;

import net.gtamps.shared.game.IGameActor;
import net.gtamps.shared.game.NullGameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventDispatcher;
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
	private static final GameEvent DIFFERENT_EVENT = new GameEvent(DIFFERENT_UNAMBIGUOUS_EVENT_TYPE, NullGameObject.DUMMY);

	@Mock
	private Entity parent;

	@Mock
	private IGameActor gameActor;

	@Mock
	private IGameEventDispatcher eventRoot;


	private final Entity realEntity = new Entity("aRealTestEntity");
	private Handler<?> handler;

	@Before
	public void createHandler() throws Exception {
		handler = createHandlerThatDelegatesEventsToMock(parent, gameActor, SAMPLE_EVENT.getType());
	}


	@Test
	public void testaddEventListenerDispatchEvent_shouldPassEventsToEventListener() {
		// setup
		final IGameEventListener listener = parent;
		handler.enable();

		// run
		handler.addEventListener(SAMPLE_EVENT.getType(), listener);
		handler.dispatchEvent(SAMPLE_EVENT);

		// assert
		verify(listener).receiveEvent(SAMPLE_EVENT);
	}


	@Test
	public void testRemoveEventListener_shouldStopEventsBeingPassed() {
		// setup
		final IGameEventListener listener = parent;
		handler.addEventListener(SAMPLE_EVENT.getType(), listener);
		handler.enable();

		// run
		handler.removeEventListener(SAMPLE_EVENT.getType(), listener);
		handler.dispatchEvent(SAMPLE_EVENT);

		// assert
		verify(listener, never()).receiveEvent(isA(GameEvent.class));
	}

	@Test
	public void testConnectUpwardsActor_whenEventTypesDeclared_shouldReceiveEventsFromActor() {
		// setup
		final IGameActor upwardsActor = realEntity;
		final Handler<?> localHandler = createHandlerThatDelegatesEventsToMock(realEntity, gameActor, SAMPLE_EVENT.getType());

		// run
		localHandler.connectUpwardsActor(upwardsActor);
		realEntity.dispatchEvent(SAMPLE_EVENT);

		// assert
		verify(gameActor).receiveEvent(SAMPLE_EVENT);
	}


	@Test
	public void testDisconnectUpwardsActor_shouldReceiveNoEventsFromActor() {
		// setup
		final IGameActor upwardsActor = realEntity;
		final Handler<?> localHandler = createHandlerThatDelegatesEventsToMock(realEntity, gameActor, SAMPLE_EVENT.getType());

		// run
		localHandler.disconnectUpwardsActor(upwardsActor);
		realEntity.dispatchEvent(SAMPLE_EVENT);

		// assert
		verify(gameActor, never()).receiveEvent(any(GameEvent.class));
	}

	@Test
	public void testGetReceivedEventTypes_shouldChangeWhichEventTypesListenedFor() {
		// setup
		createHandlerThatDelegatesEventsToMock(realEntity, gameActor, DIFFERENT_EVENT.getType());

		// run
		realEntity.dispatchEvent(DIFFERENT_EVENT);

		// assert
		verify(gameActor).receiveEvent(DIFFERENT_EVENT);
	}

	@SuppressWarnings("serial")
	private Handler<?> createHandlerThatDelegatesEventsToMock(final Entity parent, final IGameActor mock, final EventType... eventTypes) {
		return new Handler<Entity>(eventRoot, Type.AUTODISPOSE, parent) {
			@Override
			public void receiveEvent(final GameEvent event) {
				mock.receiveEvent(event);
			}

			@Override
			public Set<EventType> getReceivedEventTypes() {
				return toImmutableSet(eventTypes);
			}
		};
	}

}
