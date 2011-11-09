package net.gtamps.shared.serializer.communication;

import java.util.ArrayList;

import net.gtamps.shared.SharedObject;

import org.jetbrains.annotations.NotNull;

public class Message extends SharedObject {

	private static final long serialVersionUID = 608050744473650094L;

	@NotNull
    private volatile String sessionId;

    public final ArrayList<Sendable> sendables;

    public Message(@NotNull Sendable sendable) {
        this();
        sendables.add(sendable);
    }

    public Message() {
        sendables = new ArrayList<Sendable>();
    }

    public void addSendable(Sendable sendable) {
        sendables.add(sendable);
    }

    public
    @NotNull
    String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NotNull String sessionId) {
        this.sessionId = sessionId;
        for (Sendable s : sendables) {
            s.sessionId = sessionId;
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "sessionId='" + sessionId + '\'' +
                ", sendables=" + sendables +
                '}';
    }
}
