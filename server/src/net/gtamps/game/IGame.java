package net.gtamps.game;

import java.util.Collection;

import net.gtamps.shared.communication.Sendable;

public interface IGame {
	
	public void start();
	public void hardstop();
	public boolean isActive();
	public long getId();
	public String getName();
	public void handleSendable(Sendable r);
	void drainResponseQueue(Collection<Sendable> target);
	

}
