package net.gtamps.android.game;

import net.gtamps.shared.game.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final public class PlayerManager {

    private final List<Player> players;

    @Nullable
    private Player activePlayer;

    public PlayerManager() {
        players = new ArrayList<Player>();
    }

    public void addPlayer(@NotNull Player player) {
        players.add(player);
    }

    public void removePlayer(@NotNull Player player) {
        players.remove(player);
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void setActivePlayer(@NotNull Player player) {
        activePlayer = player;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
}
