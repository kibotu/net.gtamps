package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.CheckedShareable;
import net.gtamps.shared.SharedObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Message extends SharedObject {

    private static final long serialVersionUID = 608050744473650094L;

    @NotNull
    private volatile String sessionId;

    @CheckedShareable
    public final ArrayList<Sendable> sendables;

    public Message(@NotNull final Sendable sendable) {
        this();
        sendables.add(sendable);
    }

    public Message() {
        sendables = new ArrayList<Sendable>();
    }

    public void addSendable(final Sendable sendable) {
        sendables.add(sendable);
    }

    public
    @NotNull
    String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NotNull final String sessionId) {
        this.sessionId = sessionId;
        for (final Sendable s : sendables) {
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
