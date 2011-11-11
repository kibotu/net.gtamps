package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;
import net.gtamps.shared.game.player.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerData extends SharedObject implements ISendableData {

    private static final long serialVersionUID = -9000251941870190642L;

    @NotNull
    public final Player player;

    public PlayerData(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "{" + player.toString() + "}";
    }
}
