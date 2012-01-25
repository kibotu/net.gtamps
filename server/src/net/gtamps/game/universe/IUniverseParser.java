package net.gtamps.game.universe;

import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.physics.Box2DEngine;

public interface IUniverseParser {
    
    public Box2DEngine getPhysics();

    public Universe getWorld();

    public void populateWorld(final EntityManager em);
}
