package net.gtamps.shared.communication;

import net.gtamps.shared.Utils.UIDGenerator;


public class Request implements ISendable {

    public enum Type {
        LOGIN, REGISTER,JOIN,LEAVE,GETMAPDATA,GETPLAYER,GETUPDATE;
    }

    public final Type type;
    public Object data;
    public final int id;

    public Request(Type type) {
        this.type = type;
        id = UIDGenerator.getNewUID();
        data = null;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
