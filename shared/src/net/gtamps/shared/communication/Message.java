package net.gtamps.shared.communication;

import net.gtamps.shared.communication.commands.ACommand;
import net.gtamps.shared.communication.requests.ARequest;
import net.gtamps.shared.communication.responses.AResponse;

import java.util.ArrayList;

public class Message {

    private String sessionId;

    public final ArrayList<AResponse> responses;
    public final ArrayList<ARequest> ARequests;
    public final ArrayList<ACommand> ACommands;

    public Message() {
        responses = new ArrayList<AResponse>();
        ARequests = new ArrayList<ARequest>();
        ACommands = new ArrayList<ACommand>();
    }

    public void addRequest(ARequest ARequest) {
        ARequests.add(ARequest);
    }

    public void addCommando(ACommand ACommand) {
        ACommands.add(ACommand);
    }

    public void addResponse(AResponse response) {
        responses.add(response);
    }
}
