package net.gtamps.game.universe;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.shared.game.entity.Entity;

import org.jdom.Element;

public class MapParser {

    private final Element mapXml;

    private final String mapName = "world";
    private Universe universe;
    private Box2DEngine physics;
    private final Map<Point, Integer> spawnPoints = new HashMap<Point, Integer>();
    private final Map<Point, Integer> carPoints = new HashMap<Point, Integer>();
    private final Map<Point, Integer> buildingPoints = new HashMap<Point, Integer>();

    public MapParser(final Element mapXml) {
        if (mapXml == null) {
            throw new IllegalArgumentException("'mapXml' must not be null");
        }
        this.mapXml = mapXml;
        parseXmlMap();
    }

    public Box2DEngine getPhysics() {
        return physics;
    }

    public Universe getWorld() {
        return universe;
    }

    public void populateWorld(final EntityManager em) {
        createSpawnPoints(em);
        //createCars(em);
//		createBuildings(em);
    }

    private void parseXmlMap() {
        final List<Element> rows = mapXml.getChildren("row");
        assert rows != null && rows.size() > 0;
        List<Element> tiles = rows.get(0).getChildren("tile");
        assert tiles != null && tiles.size() > 0;

        final int tileHeight = rows.size();
        final int tileWidth = tiles.size();
        final int mapWidth = tileWidth * WorldConstants.TILE_SIZE_PIX;
        final int mapHeight = tileHeight * WorldConstants.TILE_SIZE_PIX;


        universe = new Universe(mapName, mapWidth, mapHeight);
        physics = PhysicsFactory.createPhysics(universe, mapWidth, mapHeight);
        universe.setPhysics(physics);

        for (int row = 0; row < tileHeight; row++) {
            tiles = rows.get(row).getChildren("tile");
            assert tiles.size() == tileWidth;
            for (int col = 0; col < tileWidth; col++) {
                final int posX = (int) ((col + 0.5f) * WorldConstants.TILE_SIZE_PIX);
                final int posY = (int) ((row + 0.5f) * WorldConstants.TILE_SIZE_PIX);
                final MapTile t = new MapTile(tiles.get(col));
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

    private void createSpawnPoints(final EntityManager em) {
        for (final Point p : spawnPoints.keySet()) {
            final Entity sp = em.createEntitySpawnPoint(universe, p.x, p.y, spawnPoints.get(p));
            universe.addSpawnPoint(sp);
        }
    }

    private void createCars(final EntityManager em) {
        for (final Point p : carPoints.keySet()) {
            em.createEntityCar(p.x, p.y, carPoints.get(p));
        }

    }

    private void createBuildings(final EntityManager em) {
        for (final Point p : buildingPoints.keySet()) {
            em.createEntityHouse(p.x, p.y);
        }

    }


}
