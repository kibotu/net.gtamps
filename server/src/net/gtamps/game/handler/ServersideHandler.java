package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.handler.Handler;

@SuppressWarnings("serial")
public abstract class ServersideHandler<T extends SharedGameActor> extends Handler<T> {

	public ServersideHandler(final Universe universe, final Type type, final T parent, final EventType...eventTypes) {
		super(universe, type, parent, eventTypes);
	}

	protected Universe getUniverse() {
		return (Universe) eventRoot;
	}

}
