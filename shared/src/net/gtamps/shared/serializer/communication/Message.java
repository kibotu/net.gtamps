package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.CheckedShareable;
import net.gtamps.shared.SharedObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Message extends SharedObject {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sendables == null) ? 0 : sendables.hashCode());
        result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (sendables == null) {
            if (other.sendables != null)
                return false;
        } else if (!sendables.equals(other.sendables))
            return false;
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
            return false;
        return true;
    }

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
