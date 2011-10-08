package net.gtamps.shared.communication;

import net.gtamps.shared.Utils.UIDGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Request implements ISendable {

    public enum Type {
        GETUPDATE, GETPLAYER, JOIN, LEAVE, LOGIN, REGISTER, SESSION, GETMAPDATA
    }

    @NotNull
    public final Type type;
    @Nullable
    private ISendableData data;
    public final int id;

    public Request(@NotNull Type type, @Nullable ISendableData data) {
        this.type = type;
        this.data = data;
        id = UIDGenerator.getNewUID();
    }

    public Request(Type type){
        this(type,null);
    }

    public void setData(@NotNull ISendableData data) {
        this.data = data;
    }

    public @Nullable
    ISendableData getData() {
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
