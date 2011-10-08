package net.gtamps.shared.communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    private String sessionId;
    private long revId;

    public final ArrayList<ISendable> sendables;

    public Message(ISendable sendable) {
        this();
        sendables.add(sendable);
    }

    public Message() {
        sendables = new ArrayList<ISendable>();
    }

    public void addSendable(ISendable sendable) {
        sendables.add(sendable);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getRevId() {
        return revId;
    }

    public void setRevId(long revId) {
        this.revId = revId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sessionId='" + sessionId + '\'' +
                ", sendables=" + sendables +
                '}';
    }
}
