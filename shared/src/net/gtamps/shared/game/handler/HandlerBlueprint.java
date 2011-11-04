package net.gtamps.shared.game.handler;

import net.gtamps.shared.game.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class HandlerBlueprint {

    @NotNull
    private final Handler.Type type;

    public HandlerBlueprint(@NotNull Handler.Type type) {
        this.type = type;
    }

    public Handler.Type getType() {
        return this.type;
    }

    public Handler createHandler(Entity parent) {
        return createHandler(parent, null, null, null);
    }

    public abstract Handler createHandler(Entity parent, Integer pixX, Integer pixY, Integer deg);

    public abstract HandlerBlueprint copy();

}
