package net.gtamps.game;

import java.util.Collection;

import net.gtamps.shared.serializer.communication.NewSendable;

public interface IGame {

	public void start();

	public void hardstop();

	public boolean isActive();

	public long getId();

	public String getName();

	public void handleSendable(NewSendable r);

	void drainResponseQueue(Collection<NewSendable> target);


}
