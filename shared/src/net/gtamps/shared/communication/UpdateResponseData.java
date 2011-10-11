package net.gtamps.shared.communication;

import net.gtamps.shared.game.entity.Entity;

import java.util.ArrayList;

public class UpdateResponseData implements ISendableData {

    public long revId;
    public ArrayList<Entity> entites;

    public UpdateResponseData(long revId) {
        this.revId = revId;
        entites = new ArrayList<Entity>();
    }
}
