package net.gtamps.game.player;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.server.User;
import net.gtamps.shared.game.player.Player;

public class PlayerManagerFacade {

    private final Map<User, Integer> storage = new HashMap<User, Integer>();
    private final PlayerManager playerManager;

    public PlayerManagerFacade(final PlayerManager playerManager) {
        if (playerManager == null) {
            throw new IllegalArgumentException(
                    "'playerManager' must not be null");
        }
        this.playerManager = playerManager;
    }

    public Player joinUser(final User user) {
        Player player;
        if (hasPlayerForUser(user)) {
            player = getPlayerForUser(user);
        } else {
            player = createPlayerForUser(user);
        }
        if (player.getEntity() == null) {
	        final boolean spawned = playerManager.spawnPlayer(player.getUid());
	        return spawned ? player : null;
        } else {
        	return player;
        }
    }

    public void leaveUser(final User user) {
        playerManager.deactivatePlayer(storage.get(user));
    }

    public boolean hasPlayerForUser(final User user) {
        return storage.containsKey(user);
    }

    public Player getPlayerForUser(final User user) {
        final int puid = storage.get(user);
        return playerManager.getPlayer(puid);
    }

    public Player createPlayerForUser(final User user) {
        final Player player = playerManager.createPlayer(user);
        storage.put(user, player.getUid());
        return player;
    }


}
