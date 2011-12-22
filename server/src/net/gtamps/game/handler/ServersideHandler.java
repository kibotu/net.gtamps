package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

public abstract class ServersideHandler extends Handler {

	public ServersideHandler(final Universe universe, final Type type, final Entity parent) {
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
