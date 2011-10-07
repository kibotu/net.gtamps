package net.gtamps.shared.game.entity;

import net.gtamps.shared.math.Vector3;

public class Entity {

    private String name;
    private Vector3 position;
    private Vector3 scaling;
    private Vector3 rotation;

    public Entity(String name) {

        position = Vector3.createNew(0,0,0);
        scaling = Vector3.createNew(0,0,0);
        rotation = Vector3.createNew(0,0,0);

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getScaling() {
        return scaling;
    }

    public Vector3 getRotation() {
        return rotation;
    }
}
