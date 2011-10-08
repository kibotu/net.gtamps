package net.gtamps.shared.communication;

import net.gtamps.shared.communication.ISendable;

public class Response implements ISendable {

    public enum Status {
        OK, NEED, BAD, ERROR
    }

    public final Status status;
    public final Request.Type requestType;

    public Response(Request.Type requestType, Status status) {
        this.status = status;
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", requestType=" + requestType +
                '}';
    }
}
