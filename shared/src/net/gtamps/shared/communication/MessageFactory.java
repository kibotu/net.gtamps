package net.gtamps.shared.communication;

import net.gtamps.shared.communication.data.AuthentificationData;
import net.gtamps.shared.communication.data.FloatData;
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

    public static Message createAccelerateCommand(float value) {
        return new Message(new Sendable(SendableType.ACCELERATE, new FloatData(value)));
    }

    public static Message createDecelerateCommand(float value) {
        return new Message(new Sendable(SendableType.DECELERATE, new FloatData(value)));
    }

    public static Message createRightCommand(float value) {
        return new Message(new Sendable(SendableType.RIGHT, new FloatData(value)));
    }

    public static Message createLeftCommand(float value) {
        return new Message(new Sendable(SendableType.LEFT, new FloatData(value)));
    }

    public static Message createJoinCommand() {
        return new Message(new Sendable(SendableType.ENTEREXIT));
    }

    public static Message createRegisterCommand() {
        return new Message(new Sendable(SendableType.SHOOT));
    }

    public static Message createLoginCommand() {
        return new Message(new Sendable(SendableType.HANDBRAKE));
    }
}
