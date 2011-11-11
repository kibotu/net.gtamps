package net.gtamps.game.physics;

import net.gtamps.game.conf.WorldConstants;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class Box2DEngine {
    World world;
    Body groundBody;

    /**
     * Creates a new Box2d World and stuff.
     *
     * @param lowerVertex sets the lower bounds of the world in box2d coordinates (1m =
     *                    10 px)
     * @param upperVertex sets the upper bounds of the world in box2d coordinates (1m =
     *                    10px)
     * @see WorldConstants#PIX_TO_PHYSICS_RATIO
     */
    public Box2DEngine(float minx, float miny, float maxx, float maxy) {
        Vec2 lowerVertex = new Vec2(minx, miny);
        Vec2 upperVertex = new Vec2(maxx, maxy);
        Vec2 gravity = new Vec2(0f, 0f);
        AABB aabb = new AABB(lowerVertex, upperVertex);
        world = new World(aabb, gravity, true);
        world.setContactListener(RawContactHandler.getInstance());
        world.setBoundaryListener(BoundaryListener.getInstance());

        // we should create some physical bounding box for all elements so that
        // you can't escape the world.
        // but for now it's okay without it, i guess.
        /*
           * PolygonDef polygonDef = new PolygonDef(); polygonDef.density = 0f;
           * polygonDef.friction = 0.1f; polygonDef.setAsBox(100f, 10f);
           *
           * BodyDef groundBodyDef = new BodyDef(); groundBodyDef.position.set(0f,
           * -10f); // has zero mass, therefore it is immovable groundBody =
           * world.createBody(groundBodyDef); groundBody.createShape(polygonDef);
           */

    }

    public World getWorld() {
        return this.world;
    }

    public void step(float dt, int iterations) {

        //calculate next physics engine step
        world.step(dt, iterations);
    }


}
