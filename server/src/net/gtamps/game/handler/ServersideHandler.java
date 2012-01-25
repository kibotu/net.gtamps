package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

public abstract class ServersideHandler<T extends SharedGameActor> extends Handler<T> {

	public ServersideHandler(final Universe universe, final Type type, final T parent) {
		super(universe, type, parent);
	}

	protected Universe getUniverse() {
		return (Universe) eventRoot;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		// TODO Auto-generated method stub

	}

}
