package net.gtamps.android.game;

import net.gtamps.android.game.entity.views.EntityView;
import net.gtamps.shared.game.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

final public class PlayerManager {

    private final Set<Player> players;

    @Nullable
    private Player activePlayer;

    public PlayerManager() {
        players = new HashSet<Player>();
    }

    public void addPlayer(@NotNull Player player) {
        players.add(player);
    }

    public void removePlayer(@NotNull Player player) {
        players.remove(player);
    }

    public void setActivePlayer(@NotNull Player player) {
        activePlayer = player;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
}
