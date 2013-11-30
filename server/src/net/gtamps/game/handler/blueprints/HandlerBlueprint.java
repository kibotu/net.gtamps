package net.gtamps.game.handler.blueprints;

import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.handler.Handler;

import org.jetbrains.annotations.NotNull;

public abstract class HandlerBlueprint<T extends SharedGameActor> {

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

	public abstract ServersideHandler<T> createHandler(T parent);

	public abstract HandlerBlueprint<T> copy();

}
