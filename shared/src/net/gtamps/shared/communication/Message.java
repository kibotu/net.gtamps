package net.gtamps.shared.communication;

import java.util.ArrayList;


public class Message {

    private String sessionId;

    public final ArrayList<Response> responses;
    public final ArrayList<Request> requests;
    public final ArrayList<Command> commands;

    public Message() {
        responses = new ArrayList<Response>();
        requests= new ArrayList<Request>();
        commands= new ArrayList<Command>();
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void addCommando(Command command) {
        commands.add(command);
    }

    public void addResponse(Response response) {
        responses.add(response);
    }
}
