package net.gtamps.game;

import net.gtamps.shared.serializer.communication.Sendable;

import java.util.Collection;

public interface IGame {

    public void start();

    public void hardstop();

    public boolean isActive();

    public long getId();

    public String getName();

    public void handleSendable(Sendable r);

    void drainResponseQueue(Collection<Sendable> target);


}
