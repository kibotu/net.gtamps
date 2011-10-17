package net.gtamps.shared.communication;

import net.gtamps.shared.communication.data.AuthentificationData;
import net.gtamps.shared.communication.data.RevisionData;

final public class MessageFactory {

    private MessageFactory() {
    }

    public static Message createSessionRequest() {
        return new Message(new Sendable(SendableType.SESSION));
    }

    public static Message createLoginRequest(String username, String password) {
        return new Message(new Sendable(SendableType.LOGIN, new AuthentificationData(username,password)));
    }

    public static Message createRegisterRequest(String username, String password) {
        return new Message(new Sendable(SendableType.REGISTER, new AuthentificationData(username,password)));
    }

    public static Message createJoinRequest() {
        return new Message(new Sendable(SendableType.JOIN));
    }

    public static Message createLeaveRequest() {
        return new Message(new Sendable(SendableType.LEAVE));
    }

    public static Message createGetMapDataRequest() {
        return new Message(new Sendable(SendableType.GETMAPDATA));
    }

    public static Message createGetPlayerRequest() {
        return new Message(new Sendable(SendableType.GETPLAYER));
    }

    public static Message createGetUpdateRequest(long revId) {
        return new Message(new Sendable(SendableType.GETUPDATE, new RevisionData(revId)));
    }

    public static Message createOkCommand() {
        return new Message(new Sendable(SendableType.OK));
    }

    public static Message createNeedCommand() {
        return new Message(new Sendable(SendableType.NEED));
    }

    public static Message createBadCommand() {
        return new Message(new Sendable(SendableType.BAD));
    }

    public static Message createErrorCommand() {
        return new Message(new Sendable(SendableType.ERROR));
    }
}
