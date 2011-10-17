package net.gtamps.shared.communication;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    @NotNull
    private String sessionId;

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

    public @NotNull String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NotNull String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sessionId='" + sessionId + '\'' +
                ", sendables=" + sendables +
                '}';
    }
}
