package net.gtamps.game.player;

import net.gtamps.server.gui.Logger;
import net.gtamps.server.gui.LogType;


public class PlayerFactory {

	private PlayerFactory() {
	}
	
	public static Player createPlayer(String name) {
		Logger.getInstance().log(LogType.PLAYER, "A new Player ["+name+"] was created");
		return  new Player(name);
	}
}
