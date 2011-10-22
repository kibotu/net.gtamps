package net.gtamps.shared.communication.data;

import net.gtamps.shared.game.entity.Entity;

import java.util.ArrayList;

public class UpdateData implements ISendableData {

	public transient long oldRevId;
    public long revId;
    public ArrayList<Entity> entities;

    public UpdateData(long oldRevId, long newRevId) {
        this.revId = newRevId;
        entities = null;
    }

    @Override
    public String toString() {
        return "UpdateData{" +
                "revId=" + revId +
                ", entites=" + entities +
                '}';
    }
}
