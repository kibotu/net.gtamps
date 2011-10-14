package net.gtamps.shared.communication;

import net.gtamps.shared.communication.ISendable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Response implements ISendable {

    public enum Status {
        OK, NEED, BAD, ERROR
    }

    @NotNull
    public final Status status;
    @NotNull
    public final int requestId;
    @NotNull
    public final Request.Type requestType;
    @Nullable
    private ISendableData data;

//    public Response(@NotNull Request.Type requestType, @NotNull Status status, @Nullable ISendableData data) {
//        this.status = status;
//        this.requestType = requestType;
//        this.data = data;
//    }

    public Response(@NotNull Status status, @NotNull Request request) {
    	this(status, request, null);    
    }

    
    public Response(@NotNull Status status, @NotNull Request request, @Nullable ISendableData data) {
        this.status = status;
        this.requestId = request.id;
        this.requestType = request.type;
        this.data = data;
    }

    @Nullable
    public ISendableData getData() {
        return data;
    }

    public void setData(@Nullable ISendableData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", requestType=" + requestType +
                ", data=" + data +
                '}';
    }
}
