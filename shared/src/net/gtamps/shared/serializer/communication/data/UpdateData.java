package net.gtamps.shared.serializer.communication.data;

import java.util.ArrayList;

import net.gtamps.shared.SharedObject;
import net.gtamps.shared.game.GameObject;

public class UpdateData extends SharedObject implements ISendableData {

    public transient long oldRevId;
    public long revId;
    public ArrayList<GameObject> gameObjects;

    public UpdateData(long oldRevId, long newRevId) {
        this.revId = newRevId;
        gameObjects = null;
    }

    @Override
    public String toString() {
        return "UpdateData{" +
                "revId=" + revId +
                ", entites=" + gameObjects +
                '}';
    }
}
