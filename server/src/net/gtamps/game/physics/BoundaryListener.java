package net.gtamps.game.physics;

import org.jbox2d.dynamics.Body;

public class BoundaryListener implements org.jbox2d.dynamics.BoundaryListener {

    private static final BoundaryListener INSTANCE = new BoundaryListener();


    private BoundaryListener() {

    }

    @Override
    public void violation(Body body) {
        System.out.println("" + body.getUserData().toString() + " has left the world.");

    }

    public static org.jbox2d.dynamics.BoundaryListener getInstance() {
        // TODO Auto-generated method stub
        return INSTANCE;
    }

}
