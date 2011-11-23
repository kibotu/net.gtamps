package net.gtamps.shared.game.handler;

import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.IGameEventDispatcher;

import org.jetbrains.annotations.NotNull;

public abstract class HandlerBlueprint {

	@NotNull
	protected final IGameEventDispatcher eventRoot;
	@NotNull
	private final Handler.Type type;

	public HandlerBlueprint(@NotNull final IGameEventDispatcher eventRoot, @NotNull final Handler.Type type) {
		this.eventRoot = eventRoot;
		this.type = type;
	}

	public Handler.Type getType() {
		return this.type;
	}

	public Handler createHandler(final Entity parent) {
		return createHandler(parent, null, null, null);
	}

	public abstract Handler createHandler(Entity parent, Integer pixX, Integer pixY, Integer deg);

	public abstract HandlerBlueprint copy();

}
