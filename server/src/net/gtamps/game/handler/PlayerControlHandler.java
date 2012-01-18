package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;

public class PlayerControlHandler extends ServersideHandler<Player> {

	private static EventType[] receives = {EventType.ACTION_EVENT};

	public PlayerControlHandler(final Universe universe, final Player parent) {
		super(universe, Handler.Type.PLAYER_CONTROL, parent);
		setReceives(receives);
		connectUpwardsActor(parent);
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		//TODO optimize
		//		if (event.getType().isType(EventType.ACTION_EVENT)) {
		final int uid = parent.useProperty("entity_uid", GameObject.INVALID_UID).value();
		final Entity entity = getUniverse().getEntity(uid);
		entity.receiveEvent(event);
		//		}
	}

}
