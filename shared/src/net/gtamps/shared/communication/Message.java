package net.gtamps.shared.communication;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    @NotNull
    private String sessionId;

    public final ArrayList<ISendable> sendables;

    public Message(@NotNull ISendable sendable) {
        this();
        sendables.add(sendable);
    }

    public Message() {
        sendables = new ArrayList<ISendable>();
    }

    public void addSendable(ISendable sendable) {
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
