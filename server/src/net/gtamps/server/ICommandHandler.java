package net.gtamps.server;

import net.gtamps.shared.communication.Command;

public interface ICommandHandler {
	
	public void handleCommand(Session s, Command c);

}
