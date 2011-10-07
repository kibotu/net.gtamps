package net.gtamps.shared.communication.requests;

import net.gtamps.shared.Utils.UIDGenerator;
import net.gtamps.shared.communication.ISendable;

public abstract class ARequest implements ISendable {

//    public enum Type {
//        LOGIN, REGISTER, JOIN, LEAVE, GETMAPDATA, GETUPDATE, GETPLAYER
//    }

    public final int id;

    public ARequest() {
        this.id = UIDGenerator.getNewUID();
    }
}
