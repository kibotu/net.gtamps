package net.gtamps.shared.communication;

import net.gtamps.shared.game.entity.Entity;

import java.util.ArrayList;

public class UpdateData implements ISendableData {

    public long revId;
    public ArrayList<Entity> entites;

    public UpdateData(long revId) {
        this.revId = revId;
        entites = new ArrayList<Entity>();
    }
}
