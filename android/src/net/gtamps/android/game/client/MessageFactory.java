package net.gtamps.android.game.client;

import net.gtamps.shared.communication.*;
import org.jetbrains.annotations.NotNull;

final public class MessageFactory {

    private static ISerializer serializer = new ObjectSerializer();

    private MessageFactory() {
    }

    public static Message createSessionRequest() {
        return new Message(new Request(Request.Type.SESSION));
    }

    public static Message createCommand(Command.Type type, float percent) {
        return new Message(new Command(type,percent));
    }

    public static Message createLoginRequest(String username, String password) {
        return new Message(new Request(Request.Type.LOGIN, new Authentification(username,password)));
    }

    public static Message createRegisterRequest(String username, String password) {
        return new Message(new Request(Request.Type.REGISTER, new Authentification(username,password)));
    }

    public static Message createJoinRequest() {
        return new Message(new Request(Request.Type.JOIN));
    }

    public static Message createLeaveRequest() {
        return new Message(new Request(Request.Type.LEAVE));
    }

    public static Message createGetMapDataRequest() {
        return new Message(new Request(Request.Type.GETMAPDATA));
    }

    public static Message createGetPlayerRequest() {
        return new Message(new Request(Request.Type.GETPLAYER));
    }

    public static Message createGetUpdateRequest() {
        return new Message(new Request(Request.Type.GETUPDATE, new Revision(ConnectionManager.currentRevId)));
    }

    public static byte [] serialize(@NotNull Message message) {
        message.setSessionId(ConnectionManager.currentSessionId);
        return serializer.serializeMessage(message);
    }

    public static Message deserialize(@NotNull byte[] message) {
        return serializer.deserializeMessage(message);
    }
}
