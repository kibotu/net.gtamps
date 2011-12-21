package net.gtamps.game.handler.blueprints;

import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;

import org.jetbrains.annotations.NotNull;

public abstract class HandlerBlueprint {

	@NotNull
	protected final Universe universe;
	@NotNull
	private final Handler.Type type;

	public HandlerBlueprint(@NotNull final Universe universe, @NotNull final Handler.Type type) {
		this.universe = universe;
		this.type = type;
	}

	public Handler.Type getType() {
		return type;
	}

	public ServersideHandler createHandler(final Entity parent) {
		return createHandler(parent, null, null, null);
	}

	public abstract ServersideHandler createHandler(Entity parent, Integer pixX, Integer pixY, Integer deg);

	public abstract HandlerBlueprint copy();

}
