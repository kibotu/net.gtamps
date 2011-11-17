package net.gtamps.game.universe;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.shared.game.entity.Entity;
import org.jdom.Element;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapParser {

    private final Element mapXml;

    private String mapName = "world";
    private Universe world;
    private Box2DEngine physics;
    private Map<Point, Integer> spawnPoints = new HashMap<Point, Integer>();
    private Map<Point, Integer> carPoints = new HashMap<Point, Integer>();
    private Map<Point, Integer> buildingPoints = new HashMap<Point, Integer>();

    public MapParser(Element mapXml) {
        if (mapXml == null) {
            throw new IllegalArgumentException("'mapXml' must not be null");
        }
        this.mapXml = mapXml;
        parseXmlMap();
    }

    public Box2DEngine getPhysics() {
        return this.physics;
    }

    public Universe getWorld() {
        return this.world;
    }

    public void populateWorld(EntityManager em) {
        createSpawnPoints(em);
        //createCars(em);
//		createBuildings(em);
    }

    private void parseXmlMap() {
        List<Element> rows = mapXml.getChildren("row");
        assert rows != null && rows.size() > 0;
        List<Element> tiles = rows.get(0).getChildren("tile");
        assert tiles != null && tiles.size() > 0;

        int tileHeight = rows.size();
        int tileWidth = tiles.size();
        int mapWidth = tileWidth * WorldConstants.TILE_SIZE_PIX;
        int mapHeight = tileHeight * WorldConstants.TILE_SIZE_PIX;


        physics = PhysicsFactory.createPhysics(mapWidth, mapHeight);
        world = new Universe(mapName, mapWidth, mapHeight, physics);

        for (int row = 0; row < tileHeight; row++) {
            tiles = rows.get(row).getChildren("tile");
            assert tiles.size() == tileWidth;
            for (int col = 0; col < tileWidth; col++) {
                int posX = (int) ((col + 0.5f) * WorldConstants.TILE_SIZE_PIX);
                int posY = (int) ((row + 0.5f) * WorldConstants.TILE_SIZE_PIX);
                Tile t = new Tile(tiles.get(col));
                if (t.isSpawn()) {
                    spawnPoints.put(new Point(posX, posY), t.getCarRot());
                }
                if (t.hasCar()) {
                    carPoints.put(new Point(posX, posY), t.getCarRot());
                }
                if (t.isBuilding()) {
                    buildingPoints.put(new Point(posX, posY), t.getFloors());
                }

            }
        }


    }

    private void createSpawnPoints(EntityManager em) {
        for (Point p : spawnPoints.keySet()) {
            Entity sp = em.createEntitySpawnPoint(world, p.x, p.y, spawnPoints.get(p));
            world.addSpawnPoint(sp);
        }
    }

    private void createCars(EntityManager em) {
        for (Point p : carPoints.keySet()) {
            em.createEntityCar(p.x, p.y, carPoints.get(p));
        }

    }

    private void createBuildings(EntityManager em) {
        for (Point p : buildingPoints.keySet()) {
            em.createEntityHouse(p.x, p.y);
        }

    }


}
