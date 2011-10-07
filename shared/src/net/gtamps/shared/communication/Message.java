package net.gtamps.shared.communication;

import java.util.ArrayList;

public class Message {

    private String sessionId;

    public final ArrayList<ISendable> sendables;

    public Message() {
        sendables = new ArrayList<ISendable>();
    }

    public void addSendable(ISendable sendable) {
        sendables.add(sendable);
    }

    @Override
    public String toString() {
        return "Message{" +
                "sessionId='" + sessionId + '\'' +
                ", sendables=" + sendables +
                '}';
    }
}
