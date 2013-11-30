package net.gtamps.game.player;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.shared.game.player.Player;


public class PlayerFactory {

    private PlayerFactory() {
    }

    public static Player createPlayer(String name) {
        GUILogger.getInstance().log(LogType.PLAYER, "A new Player [" + name + "] was created");
        return new Player(name);
    }
}
