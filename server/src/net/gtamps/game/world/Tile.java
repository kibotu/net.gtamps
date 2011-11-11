package net.gtamps.game.world;

import org.jdom.Element;

class Tile {

    String texture;
    boolean isSpawn;
    boolean hasCar;
    int carRot;
    int floors;

    public Tile(Element xml) {
        this.texture = xml.getAttributeValue("src");
        this.isSpawn = xml.getAttributeValue("spawn").toLowerCase().equals("true") ? true : false;
        this.hasCar = xml.getAttributeValue("car").toLowerCase().equals("true") ? true : false;
        //TODO -90 is not the solution for everything.
        this.carRot = Integer.valueOf(xml.getAttributeValue("carrot")) - 90;
        this.floors = Integer.valueOf(xml.getAttributeValue("floors"));
    }

    public String getTexture() {
        return texture;
    }

    public boolean isSpawn() {
        return isSpawn;
    }

    public boolean hasCar() {
        return hasCar;
    }

    public int getCarRot() {
        return carRot;
    }

    public int getFloors() {
        return floors;
    }

    public boolean isBuilding() {
        //funny but true!
        return this.floors > 1;
    }

}
