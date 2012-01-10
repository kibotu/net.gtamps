package net.gtamps.shared.game.handler;

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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    private static final EventType UNAMBIGUOUS_EVENT_TYPE = EventType.ACTION_ACCELERATE;
    private static final EventType DIFFERENT_UNAMBIGUOUS_EVENT_TYPE = EventType.SESSION_UPDATE;
    private static final GameEvent SAMPLE_EVENT = new GameEvent(UNAMBIGUOUS_EVENT_TYPE, NullGameObject.DUMMY);
    private static final GameEvent DIFFERENT_EVENT = new GameEvent(UNAMBIGUOUS_EVENT_TYPE, NullGameObject.DUMMY);

    @Mock
    private Entity parent;

    @Mock
    private IGameActor gameActor;

    @Mock
    private IGameEventDispatcher eventRoot;


    private final Entity realEntity = new Entity("aRealTestEntity");

    private final Type type = Handler.Type.DRIVER;
    private Handler handler;

    @Before
    public void createHandler() throws Exception {
        handler = createHandlerThatQuerysMockEnabledOnReceiveEvent(parent, gameActor);
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
    public void testConnectUpwardsActor_whenReceivesDownNotEmpty_shouldReceiveEventsFromActor() {
        // setup
        final IGameActor upwardsActor = realEntity;
        final Handler localHandler = createHandlerThatQuerysMockEnabledOnReceiveEvent(realEntity, gameActor);
        localHandler.setReceives(new EventType[]{SAMPLE_EVENT.getType()});

        // run
        localHandler.connectUpwardsActor(upwardsActor);
        realEntity.dispatchEvent(SAMPLE_EVENT);

        // assert
        verify(gameActor, atLeastOnce()).isEnabled();
    }


    @Test
    public void testDisconnectUpwardsActor_shouldReceiveNoEventsFromActor() {
        // setup
        final IGameActor upwardsActor = realEntity;
        final Handler localHandler = createHandlerThatQuerysMockEnabledOnReceiveEvent(realEntity, gameActor);
        localHandler.setReceives(new EventType[]{SAMPLE_EVENT.getType()});
        localHandler.connectUpwardsActor(upwardsActor);

        // run
        localHandler.disconnectUpwardsActor(upwardsActor);
        realEntity.dispatchEvent(SAMPLE_EVENT);

        // assert
        verify(gameActor, never()).isEnabled();
    }

    @Test
    public void testSetReceives_shouldChangeWhichEventTypesListenedFor() {
        // setup
        final IGameActor upwardsActor = realEntity;
        final Handler localHandler = createHandlerThatQuerysMockEnabledOnReceiveEvent(realEntity, gameActor);
        localHandler.setReceives(new EventType[]{SAMPLE_EVENT.getType()});
        localHandler.connectUpwardsActor(upwardsActor);
        localHandler.setReceives(new EventType[]{DIFFERENT_EVENT.getType()});

        // run
        realEntity.dispatchEvent(DIFFERENT_EVENT);

        // assert
        verify(gameActor, atLeastOnce()).isEnabled();
    }

    private Handler createHandlerThatQuerysMockEnabledOnReceiveEvent(final Entity parent, final IGameActor mock) {
        return new Handler(eventRoot, type, parent) {
            @Override
            public void receiveEvent(final GameEvent event) {
                mock.isEnabled();
            }
        };
    }

}
