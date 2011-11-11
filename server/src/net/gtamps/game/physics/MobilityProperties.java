package net.gtamps.game.physics;

public class MobilityProperties {

    public enum Type {
        CAR, HUMAN, NONE, BULLET
    }

    public final float VELOCITY_FORCE;
    public final float STEERING_FORCE;
    public final float STEERING_RADIUS;
    public final float SLIDYNESS;
    public final float MAX_SPEED;
    public final Type TYPE;

    public MobilityProperties(final float vf, final float sf, final float sr, final float sld, final float vmax, final Type type) {
        VELOCITY_FORCE = vf;
        STEERING_FORCE = sf;
        STEERING_RADIUS = sr;
        SLIDYNESS = sld;
        MAX_SPEED = vmax;
        TYPE = type;
    }

}
