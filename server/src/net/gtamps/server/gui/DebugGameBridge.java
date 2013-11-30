package net.gtamps.server.gui;

import net.gtamps.GTAMultiplayerServer;

import org.jbox2d.dynamics.World;

public final class DebugGameBridge {
	
	public static final DebugGameBridge instance = GTAMultiplayerServer.DEBUG ?  new DebugGameBridge() : null;
	
	World world = null;
	
	private DebugGameBridge() {
	}
	
	public World getAWorld() {
		return world;
	}

	public void setWorld(final World world) {
		this.world = world;
	}

}
