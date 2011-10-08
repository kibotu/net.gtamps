package net.gtamps.shared.communication;

import net.gtamps.shared.Utils.UIDGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;


public class Request implements ISendable {

    public enum Type {
        LOGIN, REGISTER,JOIN,LEAVE,GETMAPDATA,GETPLAYER,GETUPDATE;
    }

    @NotNull
    public final Type type;
    @Nullable
    public IRequestData data;
    public final int id;

    public Request(@NotNull Type type, @Nullable IRequestData data) {
        this.type = type;
        this.data = data;
        id = UIDGenerator.getNewUID();
    }

    public Request(Type type){
        this(type,null);
    }

    public void setData(@NotNull IRequestData data) {
        this.data = data;
    }

    public @Nullable IRequestData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", data=" + data +
                ", id=" + id +
                '}';
    }
}
